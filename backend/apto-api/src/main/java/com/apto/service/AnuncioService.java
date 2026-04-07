package com.apto.service;

import com.apto.dto.request.AtualizarAnuncioRequestDTO;
import com.apto.dto.request.CriarAnuncioRequestDTO;
import com.apto.dto.request.FiltroBuscaAnuncioDTO;
import com.apto.dto.response.AnuncioResponseDTO;
import com.apto.dto.response.BuscaAnuncioResponseDTO;
import com.apto.dto.response.PaginaResponseDTO;
import com.apto.exception.*;
import com.apto.model.entity.Anuncio;
import com.apto.model.entity.Locador;
import com.apto.model.entity.Moradia;
import com.apto.model.entity.Usuario;
import com.apto.model.enums.StatusAnuncio;
import com.apto.repository.AnuncioRepository;
import com.apto.repository.LocadorRepository;
import com.apto.repository.MoradiaRepository;
import com.apto.repository.UsuarioUniversitarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final MoradiaRepository moradiaRepository;
    private final LocadorRepository locadorRepository;
    private final UsuarioUniversitarioRepository universitarioRepository;

    public AnuncioService(AnuncioRepository anuncioRepository,
                          MoradiaRepository moradiaRepository,
                          LocadorRepository locadorRepository,
                          UsuarioUniversitarioRepository universitarioRepository) {
        this.anuncioRepository = anuncioRepository;
        this.moradiaRepository = moradiaRepository;
        this.locadorRepository = locadorRepository;
        this.universitarioRepository = universitarioRepository;
    }

    public AnuncioResponseDTO criar(CriarAnuncioRequestDTO dto){

        Usuario anunciante = locadorRepository.findById(dto.anuncianteId())
                .map(locador -> (Usuario) locador)
                .orElseGet(() -> universitarioRepository.findById(dto.anuncianteId())
                        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com id " + dto.anuncianteId())));

        Moradia moradia = moradiaRepository.findById(dto.moradiaId())
                .orElseThrow(() -> new MoradiaNaoEncontradaException("Moradia não encontrada com id " + dto.moradiaId()));

        if(anuncioRepository.existsByMoradia(moradia)){
            throw new MoradiaAssociadaComAnuncioException("A moradia já está associada com um anuncio.");
        }

        Anuncio anuncio = new Anuncio();
        anuncio.setAnunciante(anunciante);
        anuncio.setTitulo(dto.titulo());
        anuncio.setDescricao(dto.descricao());
        anuncio.setValorMensal(dto.valorMensal());
        anuncio.setTipoAnuncio(dto.tipoAnuncio());
        anuncio.setStatus(StatusAnuncio.ATIVO);

        anuncio.setMoradia(moradia);
        anuncio.setDataPublicacao(LocalDate.now());

        Anuncio salvar = anuncioRepository.save(anuncio);
        return toResponseDTO(salvar);
    }

    public List<AnuncioResponseDTO> listarTodos(){
        return anuncioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AnuncioResponseDTO buscarPorId(UUID id) {
        Anuncio anuncio = buscarEntidadePorId(id);
        return toResponseDTO(anuncio);
    }

    public Anuncio buscarEntidadePorId(UUID id) {
        return anuncioRepository.findById(id)
                .orElseThrow( ()-> new AnuncioNaoEncontradoException("Anuncio não encontrado com o id: " + id));
    }

    public void deletar(UUID id) {
        Anuncio anuncio = buscarEntidadePorId(id);
        anuncioRepository.delete(anuncio);
    }

    public AnuncioResponseDTO atualizar(UUID id, UUID anuncianteId, AtualizarAnuncioRequestDTO dto){
        Anuncio anuncio = buscarEntidadePorId(id);

        if(!anuncio.getAnunciante().getId().equals(anuncianteId)){
            throw new AcessoNegadoException("Usuário não tem permissão para editar este anúncio");
        }

        anuncio.setTitulo(dto.titulo());
        anuncio.setDescricao(dto.descricao());
        anuncio.setValorMensal(dto.valorMensal());
        return toResponseDTO(anuncioRepository.save(anuncio));
    }

    public AnuncioResponseDTO atualizarStatus(UUID id, StatusAnuncio status){
        Anuncio anuncio = buscarEntidadePorId(id);
        anuncio.setStatus(status);
        return toResponseDTO(anuncioRepository.save(anuncio));
    }

    public PaginaResponseDTO<BuscaAnuncioResponseDTO> buscarAnuncios(FiltroBuscaAnuncioDTO filtro, Pageable pageable) {
        Page<Anuncio> pagina = anuncioRepository.buscarComFiltros(
                filtro.valorMin(),
                filtro.valorMax(),
                filtro.bairro(),
                filtro.tipoMoradia(),
                filtro.tipoAnuncio(),
                filtro.mobiliado(),
                filtro.aceitaAnimais(),
                filtro.quantidadeVagas(),
                pageable);
        List<BuscaAnuncioResponseDTO> conteudo = pagina.getContent()
                .stream()
                .map(this::toBuscaResponseDTO)
                .toList();
        return new PaginaResponseDTO<>(
                conteudo,
                pagina.getNumber(),
                pagina.getTotalPages(),
                pagina.getTotalElements(),
                pagina.getSize()
        );
    }

    private BuscaAnuncioResponseDTO toBuscaResponseDTO(Anuncio anuncio) {
        Moradia moradia = anuncio.getMoradia();
        return new BuscaAnuncioResponseDTO(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorMensal(),
                anuncio.getTipoAnuncio(),
                anuncio.getStatus(),
                anuncio.getDataPublicacao(),
                moradia.getId(),
                moradia.getTipoMoradia(),
                moradia.getBairro(),
                moradia.getEnderecoResumo(),
                moradia.isMobiliado(),
                moradia.isAceitaAnimais(),
                moradia.getQuantidadeVagas(),
                anuncio.getAnunciante().getNome()
        );
    }

    private AnuncioResponseDTO toResponseDTO(Anuncio anuncio) {
        return new AnuncioResponseDTO(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorMensal(),
                anuncio.getTipoAnuncio(),
                anuncio.getStatus(),
                anuncio.getDataPublicacao(),
                anuncio.getAnunciante().getId(),
                anuncio.getMoradia().getId()
        );
    }
}

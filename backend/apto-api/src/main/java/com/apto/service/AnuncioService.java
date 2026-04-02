package com.apto.service;

import com.apto.dto.request.AtualizarAnuncioRequestDTO;
import com.apto.dto.request.CriarAnuncioRequestDTO;
import com.apto.dto.response.AnuncioResponseDTO;
import com.apto.exception.*;
import com.apto.model.entity.Anuncio;
import com.apto.model.entity.Moradia;
import com.apto.model.entity.Usuario;
import com.apto.model.enums.StatusAnuncio;
import com.apto.repository.AnuncioRepository;
import com.apto.repository.LocadorRepository;
import com.apto.repository.MoradiaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final MoradiaRepository moradiaRepository;
    private final LocadorRepository locadorRepository;
    public AnuncioService(AnuncioRepository anuncioRepository,
                          MoradiaRepository moradiaRepository,
                          LocadorRepository locadorRepository) {
        this.anuncioRepository = anuncioRepository;
        this.moradiaRepository = moradiaRepository;
        this.locadorRepository = locadorRepository;
    }

    public AnuncioResponseDTO criar(CriarAnuncioRequestDTO dto){
        Anuncio anuncio = new Anuncio();

        anuncio.setTitulo(dto.titulo());
        anuncio.setDescricao(dto.descricao());
        anuncio.setValorMensal(dto.valorMensal());
        anuncio.setTipoAnuncio(dto.tipoAnuncio());
        anuncio.setStatus(StatusAnuncio.ATIVO);

        Moradia moradia = moradiaRepository.findById(dto.moradiaId())
                .orElseThrow(() -> new MoradiaNaoEncontradaException("Moradia não encontrada com id " + dto.moradiaId()));
        anuncio.setMoradia(moradia);

        Usuario anunciante = locadorRepository.findById(dto.anuncianteId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Locador não encontrado com id: " + dto.anuncianteId()));

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

    public AnuncioResponseDTO atualizar(UUID id, AtualizarAnuncioRequestDTO dto){
        Anuncio anuncio = buscarEntidadePorId(id);

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

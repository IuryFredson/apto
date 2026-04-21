package com.apto.service;

import com.apto.dto.request.AtualizarDenunciaRequestDTO;
import com.apto.dto.request.CriarDenunciaRequestDTO;
import com.apto.dto.response.DenunciaResponseDTO;
import com.apto.exception.*;
import com.apto.model.entity.*;
import com.apto.model.enums.StatusDenuncia;
import com.apto.repository.AnuncioRepository;
import com.apto.repository.DenunciaRepository;
import com.apto.repository.LocadorRepository;
import com.apto.repository.UsuarioUniversitarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DenunciaService {
    private final DenunciaRepository denunciaRepository;
    private final UsuarioUniversitarioRepository universitarioRepository;
    private final LocadorRepository locadorRepository;
    private final AnuncioRepository anuncioRepository;

    public DenunciaService(DenunciaRepository denunciaRepository, UsuarioUniversitarioRepository universitarioRepository, LocadorRepository locadorRepository, AnuncioRepository anuncioRepository) {
        this.denunciaRepository = denunciaRepository;
        this.universitarioRepository = universitarioRepository;
        this.locadorRepository = locadorRepository;
        this.anuncioRepository = anuncioRepository;
    }

    public List<DenunciaResponseDTO> listarTodas(){
        return denunciaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public DenunciaResponseDTO criar(CriarDenunciaRequestDTO dto){
        Usuario denunciante = locadorRepository.findById(dto.denuncianteId())
                .map(locador -> (Usuario) locador)
                .orElseGet(() -> universitarioRepository.findById(dto.denuncianteId())
                        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com id " + dto.denuncianteId())));

        Anuncio anuncio = anuncioRepository.findById(dto.anuncioId())
                .orElseThrow(() -> new AnuncioNaoEncontradoException("Anuncio não encontrado com id " + dto.anuncioId()));


        Denuncia denuncia = new Denuncia();
        denuncia.setStatusDenuncia(StatusDenuncia.PENDENTE);
        denuncia.setStatusAtualizadoEm(LocalDateTime.now());
        denuncia.setAnuncio(anuncio);
        denuncia.setDenunciante(denunciante);
        denuncia.setTitulo(dto.titulo());
        denuncia.setCorpo(dto.corpo());
        denuncia.setCriadoEm(LocalDateTime.now());
        return toResponseDTO(denunciaRepository.save(denuncia));
    }

    public DenunciaResponseDTO atualizar(UUID id, AtualizarDenunciaRequestDTO dto){
        Denuncia denuncia = buscarEntidadePorId(id);

        if(denuncia.getStatusDenuncia() == StatusDenuncia.ARQUIVADA){
            throw new IllegalStateException("A denuncia já está arquivada, não poderá ser alterada.");
        }

        Anuncio anuncio = anuncioRepository.findById(dto.anuncioId())
                .orElseThrow(() -> new AnuncioNaoEncontradoException("Anuncio não encontrado com id " + dto.anuncioId()));
        denuncia.setAnuncio(anuncio);
        return toResponseDTO(denunciaRepository.save(denuncia));
    }

    public DenunciaResponseDTO atualizarStatus(UUID id, StatusDenuncia novoStatus){
        Denuncia denuncia = buscarEntidadePorId(id);
        StatusDenuncia statusAtual = denuncia.getStatusDenuncia();
        if(!transicaoValida(statusAtual, novoStatus)){
            throw new TransicaoInvalidaStatusException("Transição inválida de Status " + statusAtual + " para " + novoStatus + ".");
        }
        denuncia.setStatusDenuncia(novoStatus);
        denuncia.setStatusAtualizadoEm(LocalDateTime.now());
        return toResponseDTO(denunciaRepository.save(denuncia));
    }

    public void deletar(UUID id){
        Denuncia denuncia = buscarEntidadePorId(id);
        denunciaRepository.delete(denuncia);
    }

    public Denuncia buscarEntidadePorId(UUID id){
        return denunciaRepository.findById(id)
                .orElseThrow( () -> new DenunciaNaoEncontradaException("Denuncia não encontrada com id " + id));
    }

    public DenunciaResponseDTO buscarPorId(UUID id){
        Denuncia denuncia = buscarEntidadePorId(id);
        return toResponseDTO(denuncia);
    }

    private DenunciaResponseDTO toResponseDTO(Denuncia denuncia) {
        return new DenunciaResponseDTO(
                denuncia.getId(),
                denuncia.getDenunciante().getId(),
                denuncia.getAnuncio().getId(),
                denuncia.getTitulo(),
                denuncia.getCorpo(),
                denuncia.getStatusDenuncia(),
                denuncia.getCriadoEm()
        );
    }

    //valida se a ida do status atual para outro novo é válida
    private boolean transicaoValida(StatusDenuncia atual, StatusDenuncia novo) {
        switch (atual) {
            case PENDENTE:
                return novo == StatusDenuncia.EM_ANALISE;
            case EM_ANALISE:
                return novo == StatusDenuncia.PROCEDENTE || novo == StatusDenuncia.IMPROCEDENTE;
            case PROCEDENTE:
            case IMPROCEDENTE:
                return novo == StatusDenuncia.ARQUIVADA;
            case ARQUIVADA:
                return false;
            default:
                return false;
        }
    }
}

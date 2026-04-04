package com.apto.controller;

import com.apto.dto.request.*;
import com.apto.dto.response.AnuncioResponseDTO;
import com.apto.model.enums.StatusAnuncio;
import com.apto.service.AnuncioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {
    private final AnuncioService anuncioService;

    public AnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @GetMapping
    public ResponseEntity<List<AnuncioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(anuncioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(anuncioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AnuncioResponseDTO> criar(@Valid @RequestBody CriarAnuncioRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestParam UUID anuncianteId,
            @Valid @RequestBody AtualizarAnuncioRequestDTO dto){
        return ResponseEntity.ok(anuncioService.atualizar(id, anuncianteId, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AnuncioResponseDTO> atualizarStatus(@PathVariable UUID id,@RequestBody StatusAnuncio novoStatus){
        return ResponseEntity.ok(anuncioService.atualizarStatus(id, novoStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> deletar(@PathVariable UUID id){
        anuncioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

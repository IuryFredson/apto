package com.apto.dto.request;

import com.apto.model.enums.StatusDenuncia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CriarDenunciaRequestDTO(

    @NotBlank
    UUID denuncianteId,

    @NotBlank
    UUID anuncioId,

    @Size(min = 2, max = 255)
    @NotBlank
    String titulo,
    @Size(min = 2, max = 1000)
    @NotBlank
    String corpo,

    @NotBlank
    StatusDenuncia statusDenuncia
){}
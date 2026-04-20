package com.apto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AtualizarDenunciaRequestDTO(
        @Size(min = 2, max = 255)
        @NotBlank
        String titulo,
        @Size(min = 2, max = 1000)
        @NotBlank
        String corpo,

        @NotNull
        UUID anuncioId,

        @NotNull
        UUID denuncianteId

) {}

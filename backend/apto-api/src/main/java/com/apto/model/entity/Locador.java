package com.apto.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "locadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Locador extends Usuario {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String documentoIdentificacao;

    @NotBlank
    @Column(nullable = false)
    private String nomeExibicaoOuRazao;
}
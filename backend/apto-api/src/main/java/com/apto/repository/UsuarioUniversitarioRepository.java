package com.apto.repository;

import com.apto.model.entity.UsuarioUniversitario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioUniversitarioRepository extends JpaRepository<UsuarioUniversitario, UUID> {

    boolean existsByEmail(String email);

    boolean existsByEmailInstitucional(String emailInstitucional);
}
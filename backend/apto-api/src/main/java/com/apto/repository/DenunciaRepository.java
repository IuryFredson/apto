package com.apto.repository;

import com.apto.model.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DenunciaRepository extends JpaRepository<Denuncia, UUID> {
    boolean existsByDenunciante_Id(UUID denuncianteId);
    boolean existsByAnuncio_Id(UUID anuncioId);
}

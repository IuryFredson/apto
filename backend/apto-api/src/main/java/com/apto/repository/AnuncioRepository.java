package com.apto.repository;

import com.apto.model.entity.Anuncio;
import com.apto.model.entity.Moradia;
import com.apto.model.entity.Usuario;
import com.apto.model.enums.StatusAnuncio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, UUID> {
    Optional<Anuncio> getAnuncioByAnunciante_Id(UUID usuarioId);
    Boolean existsByMoradia(Moradia moradia);
    Page<Anuncio> findByStatus(StatusAnuncio status, Pageable pageable);
}

package com.apto.service;

import com.apto.model.entity.*;
import com.apto.model.enums.*;
import com.apto.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DenunciaServiceTest {

    @Mock
    private AnuncioRepository anuncioRepository;
    @Mock
    private LocadorRepository locadorRepository;
    @Mock
    private UsuarioUniversitarioRepository usuarioUniversitarioRepository;
    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private DenunciaService denunciaService;

    @BeforeEach
    void setUp() {
        Locador usuario = new Locador();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Gabriel Silva");
        usuario.setEmail("gabriel@email.com");
        usuario.setDocumentoIdentificacao("12345678900");
        usuario.setNomeExibicaoOuRazao("Gabriel Imóveis");

        Moradia moradia = new Moradia();
        moradia.setId(UUID.randomUUID());
        moradia.setBairro("Centro");
        moradia.setTipoMoradia(TipoMoradia.APARTAMENTO);
        moradia.setMobiliado(true);
        moradia.setAceitaAnimais(false);
        moradia.setQuantidadeVagas(1);

        Anuncio anuncio = new Anuncio();
        anuncio.setId(UUID.randomUUID());
        anuncio.setTitulo("Apto centro");
        anuncio.setDescricao("Ótima localização");
        anuncio.setValorMensal(new BigDecimal("1200.00"));
        anuncio.setTipoAnuncio(TipoAnuncio.IMOVEL_COMPLETO);
        anuncio.setStatus(StatusAnuncio.ATIVO);
        anuncio.setDataPublicacao(LocalDate.now());
        Locador locadorAnunciante = new Locador();
        locadorAnunciante.setId(UUID.randomUUID());

        locadorAnunciante.setNome("João Silva");

        anuncio.setAnunciante(locadorAnunciante);
        anuncio.setMoradia(moradia);

        Denuncia denuncia = new Denuncia();

        denuncia.setId(UUID.randomUUID());
        denuncia.setAnuncio(anuncio);
        denuncia.setDenunciante(usuario);
        denuncia.setTitulo("Titulo da denuncia");
        denuncia.setCorpo("Corpo da denuncia");
        denuncia.setStatusDenuncia(StatusDenuncia.PENDENTE);
        denuncia.setCriadoEm(LocalDateTime.now());
        denuncia.setStatusAtualizadoEm(LocalDateTime.now());

    }

}

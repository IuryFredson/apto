package com.apto.service;

import com.apto.dto.response.BuscaAnuncioResponseDTO;
import com.apto.dto.response.PaginaResponseDTO;
import com.apto.model.entity.Anuncio;
import com.apto.model.entity.Locador;
import com.apto.model.entity.Moradia;
import com.apto.model.enums.StatusAnuncio;
import com.apto.model.enums.TipoAnuncio;
import com.apto.model.enums.TipoMoradia;
import com.apto.repository.AnuncioRepository;
import com.apto.repository.LocadorRepository;
import com.apto.repository.MoradiaRepository;
import com.apto.repository.UsuarioUniversitarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnuncioServiceBuscaTest {

    @Mock
    private AnuncioRepository anuncioRepository;

    @Mock
    private MoradiaRepository moradiaRepository;

    @Mock
    private LocadorRepository locadorRepository;

    @Mock
    private UsuarioUniversitarioRepository universitarioRepository;

    @InjectMocks
    private AnuncioService anuncioService;

    private Locador locador;
    private Moradia moradia;
    private Anuncio anuncio;

    @BeforeEach
    void setUp() {
        locador = new Locador();
        locador.setId(UUID.randomUUID());
        locador.setNome("João Silva");
        locador.setEmail("joao@email.com");
        locador.setDocumentoIdentificacao("12345678900");
        locador.setNomeExibicaoOuRazao("João Imóveis");

        moradia = new Moradia();
        moradia.setId(UUID.randomUUID());
        moradia.setTipoMoradia(TipoMoradia.APARTAMENTO);
        moradia.setBairro("Centro");
        moradia.setEnderecoResumo("Rua das Flores, 123");
        moradia.setMobiliado(true);
        moradia.setAceitaAnimais(false);
        moradia.setQuantidadeVagas(2);
        moradia.setRegrasMoradia("Sem festas após 22h");

        anuncio = new Anuncio();
        anuncio.setId(UUID.randomUUID());
        anuncio.setTitulo("Apartamento no centro");
        anuncio.setDescricao("Ótimo apartamento");
        anuncio.setValorMensal(new BigDecimal("850.00"));
        anuncio.setTipoAnuncio(TipoAnuncio.IMOVEL_COMPLETO);
        anuncio.setStatus(StatusAnuncio.ATIVO);
        anuncio.setDataPublicacao(LocalDate.now());
        anuncio.setAnunciante(locador);
        anuncio.setMoradia(moradia);
    }

    @Test
    void buscarAnuncios_deveRetornarPaginaComAnunciosAtivos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Anuncio> page = new PageImpl<>(List.of(anuncio), pageable, 1);

        when(anuncioRepository.findByStatus(StatusAnuncio.ATIVO, pageable)).thenReturn(page);

        PaginaResponseDTO<BuscaAnuncioResponseDTO> resultado = anuncioService.buscarAnuncios(pageable);

        assertEquals(1, resultado.conteudo().size());
        assertEquals(0, resultado.paginaAtual());
        assertEquals(1, resultado.totalPaginas());
        assertEquals(1, resultado.totalElementos());
        assertEquals(10, resultado.tamanhoPagina());

        BuscaAnuncioResponseDTO dto = resultado.conteudo().get(0);
        assertEquals(anuncio.getTitulo(), dto.titulo());
        assertEquals(anuncio.getValorMensal(), dto.valorMensal());
        assertEquals(moradia.getBairro(), dto.bairro());
        assertEquals(locador.getNome(), dto.nomeAnunciante());

        verify(anuncioRepository).findByStatus(StatusAnuncio.ATIVO, pageable);
    }

    @Test
    void buscarAnuncios_deveRetornarPaginaVaziaQuandoNaoHaAnuncios() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Anuncio> pageVazia = new PageImpl<>(List.of(), pageable, 0);

        when(anuncioRepository.findByStatus(StatusAnuncio.ATIVO, pageable)).thenReturn(pageVazia);

        PaginaResponseDTO<BuscaAnuncioResponseDTO> resultado = anuncioService.buscarAnuncios(pageable);

        assertTrue(resultado.conteudo().isEmpty());
        assertEquals(0, resultado.totalElementos());
        assertEquals(0, resultado.totalPaginas());
    }

    @Test
    void buscarAnuncios_deveMapearCamposDaMoradiaCorretamente() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Anuncio> page = new PageImpl<>(List.of(anuncio), pageable, 1);

        when(anuncioRepository.findByStatus(StatusAnuncio.ATIVO, pageable)).thenReturn(page);

        BuscaAnuncioResponseDTO dto = anuncioService.buscarAnuncios(pageable).conteudo().get(0);

        assertEquals(moradia.getId(), dto.moradiaId());
        assertEquals(moradia.getTipoMoradia(), dto.tipoMoradia());
        assertEquals(moradia.getEnderecoResumo(), dto.enderecoResumo());
        assertEquals(moradia.isMobiliado(), dto.mobiliado());
        assertEquals(moradia.isAceitaAnimais(), dto.aceitaAnimais());
        assertEquals(moradia.getQuantidadeVagas(), dto.quantidadeVagas());
    }

    @Test
    void buscarAnuncios_deveRespeitarPaginacao() {
        Pageable pageable = PageRequest.of(2, 5);
        Page<Anuncio> page = new PageImpl<>(List.of(anuncio), pageable, 11);

        when(anuncioRepository.findByStatus(StatusAnuncio.ATIVO, pageable)).thenReturn(page);

        PaginaResponseDTO<BuscaAnuncioResponseDTO> resultado = anuncioService.buscarAnuncios(pageable);

        assertEquals(2, resultado.paginaAtual());
        assertEquals(5, resultado.tamanhoPagina());
        assertEquals(11, resultado.totalElementos());
        assertEquals(3, resultado.totalPaginas());
    }
}

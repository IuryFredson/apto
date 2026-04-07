package com.apto.service;

import com.apto.dto.request.AtualizarAnuncioRequestDTO;
import com.apto.dto.request.CriarAnuncioRequestDTO;
import com.apto.dto.response.AnuncioResponseDTO;
import com.apto.exception.*;
import com.apto.model.entity.Anuncio;
import com.apto.model.entity.Locador;
import com.apto.model.entity.Moradia;
import com.apto.model.entity.Usuario;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnuncioServiceTest {

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

    private UUID anuncianteId;
    private UUID moradiaId;
    private UUID anuncioId;

    private Usuario anunciante;
    private Moradia moradia;
    private Anuncio anuncio;

    private CriarAnuncioRequestDTO criarDTO;
    private AtualizarAnuncioRequestDTO atualizarDTO;

    @BeforeEach
    void setUp() {

        //gerando dados
        anuncianteId = UUID.randomUUID();
        moradiaId = UUID.randomUUID();
        anuncioId = UUID.randomUUID();
        anunciante = new Locador();
        anunciante.setId(anuncianteId);
        anunciante.setNome("Uirá Kulesza");

        moradia = new Moradia();
        moradia.setId(moradiaId);
        moradia.setBairro("Centro");
        moradia.setTipoMoradia(TipoMoradia.APARTAMENTO);
        moradia.setMobiliado(true);
        moradia.setAceitaAnimais(false);
        moradia.setQuantidadeVagas(1);

        anuncio = new Anuncio();
        anuncio.setId(anuncioId);
        anuncio.setTitulo("Apto centro");
        anuncio.setDescricao("Ótima localização");
        anuncio.setValorMensal(new BigDecimal("1200.00"));
        anuncio.setTipoAnuncio(TipoAnuncio.IMOVEL_COMPLETO);
        anuncio.setStatus(StatusAnuncio.ATIVO);
        anuncio.setDataPublicacao(LocalDate.now());
        anuncio.setAnunciante(anunciante);
        anuncio.setMoradia(moradia);

        criarDTO = new CriarAnuncioRequestDTO(
                "Apto centro",
                "Ótima localização",
                new BigDecimal("1200.00"),
                TipoAnuncio.IMOVEL_COMPLETO,
                moradiaId,
                anuncianteId
        );

        atualizarDTO = new AtualizarAnuncioRequestDTO(
                "Titulo novo",
                "Descrição nova",
                new BigDecimal("1500.00"),
                TipoAnuncio.IMOVEL_COMPLETO
        );
    }

    @Test
    void deveCriarAnuncioComDadosValidos(){
        when(locadorRepository.findById(anuncianteId)).thenAnswer(invocation -> Optional.of(anunciante));
        when(moradiaRepository.findById(moradiaId)).thenReturn(Optional.of(moradia));
        when(anuncioRepository.existsByMoradia(moradia)).thenReturn(false);
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        AnuncioResponseDTO response = anuncioService.criar(criarDTO);

        assertNotNull(response);
        assertEquals(anuncioId, response.id());
        assertEquals(StatusAnuncio.ATIVO, response.status());
        verify(anuncioRepository).save(any(Anuncio.class));
    }

    @Test
    void naoDeveCriarAnuncioSeMoradiaNaoEncontrada(){
        when(locadorRepository.findById(anuncianteId)).thenAnswer(invocation -> Optional.of(anunciante));
        when(moradiaRepository.findById(moradiaId)).thenReturn(Optional.empty());

        assertThrows(MoradiaNaoEncontradaException.class, () -> anuncioService.criar(criarDTO));
        verify(anuncioRepository, never()).save(any());
    }

    @Test
    void naoDeveCriarAnuncioSeMoradiaJaAssociada(){
        when(locadorRepository.findById(anuncianteId)).thenAnswer(invocation -> Optional.of(anunciante));
        when(moradiaRepository.findById(moradiaId)).thenReturn(Optional.of(moradia));
        when(anuncioRepository.existsByMoradia(moradia)).thenReturn(true);

        assertThrows(MoradiaAssociadaComAnuncioException.class, () -> anuncioService.criar(criarDTO));
        verify(anuncioRepository, never()).save(any());

    }

    @Test
    public void naoDeveCriarAnuncioSeUsuarioNaoEncontrado(){
        when(locadorRepository.findById(anuncianteId)).thenReturn(Optional.empty());
        when(universitarioRepository.findById(anuncianteId)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> anuncioService.criar(criarDTO));

        verify(anuncioRepository, never()).save(any());
    }


    @Test
    void deveListarAnuncios() {
        when(anuncioRepository.findAll()).thenReturn(List.of(anuncio));
        var result = anuncioService.listarTodos();
        assertEquals(1, result.size());
        verify(anuncioRepository).findAll();
    }

    @Test
    void deveDeletarAnuncioValido() {
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.of(anuncio));

        anuncioService.deletar(anuncioId);

        verify(anuncioRepository).delete(anuncio);
    }

    @Test
    void nãoDeveDeletarAnuncioInexistente(){
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.empty());
        assertThrows(AnuncioNaoEncontradoException.class, () -> anuncioService.deletar(anuncioId));
        verify(anuncioRepository, never()).delete(any());
    }

    @Test
    void deveAtualizarAnuncioComDadosValidos() {
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        AnuncioResponseDTO response = anuncioService.atualizar(anuncioId, anuncianteId, atualizarDTO);

        assertNotNull(response);
        verify(anuncioRepository).save(anuncio);
    }

    @Test
    void naoDeveAtualizarCasoAnuncianteSejaDiferenteDoCriador() {
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.of(anuncio));

        UUID outroId = UUID.randomUUID();

        assertThrows(AcessoNegadoException.class,
                () -> anuncioService.atualizar(anuncioId, outroId, atualizarDTO));
        verify(anuncioRepository, never()).save(any());
    }

    @Test
    void naoDeveAtualizarCasoAnuncioNaoExista() {
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.empty());

        assertThrows(AnuncioNaoEncontradoException.class,
                () -> anuncioService.atualizar(anuncioId, anuncianteId, atualizarDTO));
    }

    @Test
    void deveAtualizarStatusComAnuncioEncontrado() {
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.of(anuncio));
        when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

        AnuncioResponseDTO response = anuncioService.atualizarStatus(anuncioId, StatusAnuncio.PAUSADO);

        assertNotNull(response);
        verify(anuncioRepository).save(anuncio);
    }

    @Test
    void naoDeveAtualizarStatusCasoAnuncioNaoExista() {
        when(anuncioRepository.findById(anuncioId)).thenReturn(Optional.empty());

        assertThrows(AnuncioNaoEncontradoException.class,
                () -> anuncioService.atualizarStatus(anuncioId, StatusAnuncio.PAUSADO));
    }
}

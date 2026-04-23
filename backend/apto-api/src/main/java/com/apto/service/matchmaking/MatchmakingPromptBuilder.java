package com.apto.service.matchmaking;

import com.apto.model.entity.UsuarioUniversitario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchmakingPromptBuilder {

    private final ObjectMapper objectMapper;

    public String montarSystemPrompt() {
        return """
            Você é um assessor de compatibilidade de colegas de moradia estudantil.
            Receberá um JSON com o perfil do solicitante e uma lista de candidatos, todos com campos determinísticos.
            Retorne **apenas** JSON no formato:
            {"matches": [{"candidatoId": "<uuid>", "percentual": <0-100>, "justificativa": "<1 frase curta em pt-BR>"}]}
            ordenado por `percentual` desc.
            REGRAS OBRIGATÓRIAS:
            - Inclua TODOS os candidatos recebidos, sem exceção, mesmo os muito incompatíveis.
            - Percentual deve ser entre 0 e 100.
            - Justificativas em no máximo 180 caracteres, citando 1–3 pontos de convergência ou atrito.
            - Nunca omita um candidato da lista.
            """;
    }

    public String montarUserPrompt(UsuarioUniversitario solicitante, List<UsuarioUniversitario> candidatos) {
        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("solicitante", extrairCamposDeterministicos(solicitante, false));

            List<Map<String, Object>> listaCandidatos = candidatos.stream()
                    .map(c -> extrairCamposDeterministicos(c, true))
                    .toList();

            payload.put("candidatos", listaCandidatos);

            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("Erro ao serializar prompt de matchmaking", e);
            throw new RuntimeException("Falha ao construir prompt para LLM", e);
        }
    }

    private Map<String, Object> extrairCamposDeterministicos(UsuarioUniversitario usuario, boolean incluirCandidatoId) {
        var perfil = usuario.getPerfilConvivencia();
        Map<String, Object> campos = new LinkedHashMap<>();

        if (incluirCandidatoId) {
            campos.put("candidatoId", usuario.getId().toString());
        }

        campos.put("genero", usuario.getGenero());
        campos.put("horarioSono", perfil.getHorarioSono());
        campos.put("nivelBarulhoAceitavel", perfil.getNivelBarulhoAceitavel());
        campos.put("frequenciaVisitas", perfil.getFrequenciaVisitas());
        campos.put("nivelOrganizacao", perfil.getNivelOrganizacao());
        campos.put("rotinaEstudos", perfil.getRotinaEstudos());
        campos.put("consomeAlcool", perfil.getConsomeAlcool());
        campos.put("fumante", perfil.getConsomeAlcool());
        campos.put("aceitaAnimais", perfil.getAceitaAnimais());
        campos.put("preferenciaGeneroConvivencia", perfil.getPreferenciaGeneroConvivencia());

        return campos;
    }
}
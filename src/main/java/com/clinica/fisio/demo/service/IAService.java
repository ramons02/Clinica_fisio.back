package com.clinica.fisio.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IAService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public IAService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String gerarRelatorioDeTexto(String textoTranscrito) {
        // Usando a URL e o modelo que funcionaram no seu terminal (v1beta + gemini-2.5-flash)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey.trim();
        
        System.out.println("Enviando para o Gemini 2.5 Flash...");

        // Montando o corpo exatamente como o Google exige
        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", "Aja como um Fisioterapeuta. Organize a transcrição de forma profissional para prontuário: " + textoTranscrito)
                ))
            )
        );

        try {
            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON) // Garante que o Google entenda o JSON
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::extrairTextoDaResposta)
                    .block();
        } catch (Exception e) {
            // Se der erro 400, 429 ou 404, ele vai cair aqui
            System.err.println("Erro na chamada do Gemini: " + e.getMessage());
            return "Erro ao processar com a IA: " + e.getMessage();
        }
    }

    private String extrairTextoDaResposta(Map response) {
        try {
            if (response == null || response.containsKey("error")) {
                return "O Google retornou um erro: " + response.get("error");
            }

            // Navegando no JSON que o Gemini envia de volta
            List candidates = (List) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "Nenhuma resposta gerada.";
            
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);
            
            return (String) firstPart.get("text");
        } catch (Exception e) {
            return "Erro ao extrair texto do relatório: " + e.getMessage();
        }
    }
}
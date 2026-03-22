package com.clinica.fisio.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.fisio.demo.service.IAService;

@RestController
@RequestMapping("/api/ia")
@CrossOrigin(origins = "http://localhost:4200")
public class IAController {
    @Autowired
    private IAService iaService;

    @PostMapping("/gerar-relatorio")
    public ResponseEntity<Map<String, String>> criarRelatorio(@RequestBody Map<String, String> payload) {
        String textoBruto = payload.get("texto");
        
        // A mágica acontece aqui
        String relatorioFinal = iaService.gerarRelatorioDeTexto(textoBruto);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("relatorio", relatorioFinal);
        
        return ResponseEntity.ok(resposta);
    }
    
}

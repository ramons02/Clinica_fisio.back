package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.model.Configuracao;
import com.clinica.fisio.demo.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracoes")
@CrossOrigin(origins = "*") // Para o Angular conseguir acessar
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository repository;

    @PostMapping("/financeiro")
    public ResponseEntity<Configuracao> salvarFinanceiro(@RequestBody Configuracao novaConfig) {
        // Busca a configuração atual (ID 1) ou cria uma nova
        Configuracao config = repository.findById(1L).orElse(new Configuracao());

        config.setMensalPreOp(novaConfig.getMensalPreOp());
        config.setMensalPosOp(novaConfig.getMensalPosOp());

        return ResponseEntity.ok(repository.save(config));
    }

    @GetMapping("/financeiro")
    public ResponseEntity<Configuracao> buscarFinanceiro() {
        return ResponseEntity.ok(repository.findById(1L).orElse(new Configuracao()));
    }
}

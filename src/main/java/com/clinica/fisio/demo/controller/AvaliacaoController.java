package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.model.Avaliacao;
import com.clinica.fisio.demo.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin("*")
public class AvaliacaoController {

    private final AvaliacaoRepository repository;

    public AvaliacaoController(AvaliacaoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Avaliacao salvar(@RequestBody Avaliacao avaliacao) {
        return repository.save(avaliacao);
    }

    @GetMapping
    public List<Avaliacao> listar() {
        return repository.findAll();
    }
}

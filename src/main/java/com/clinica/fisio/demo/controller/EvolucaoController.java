package com.clinica.fisio.demo.controller; // Verifique se o package está igual aos outros seus

import com.clinica.fisio.demo.model.Evolucao;
import com.clinica.fisio.demo.repository.EvolucaoRepository; // Você vai precisar criar esse também
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evolucoes")
@CrossOrigin("*") // Libera o acesso para o Angular
public class EvolucaoController {

    @Autowired
    private EvolucaoRepository repository;

    @GetMapping("/{pacienteId}")
    public List<Evolucao> listarPorPaciente(@PathVariable Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    @PostMapping
    public Evolucao salvar(@RequestBody Evolucao evolucao) {
        return repository.save(evolucao);
    }
}
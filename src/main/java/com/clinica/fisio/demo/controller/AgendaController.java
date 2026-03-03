package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.model.Agenda;
import com.clinica.fisio.demo.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
// IMPORTANTE: Liberar o DELETE aqui também!
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class AgendaController {

    @Autowired
    private AgendaRepository repository;

    @GetMapping
    public List<Agenda> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Agenda salvar(@RequestBody Agenda agenda) {
        return repository.save(agenda);
    }

    // MÉTODO PARA DESMARCAR (DELETAR)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                System.out.println("Sessão desmarcada: " + id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
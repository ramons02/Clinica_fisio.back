package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.model.Avaliacao;
import com.clinica.fisio.demo.repository.AvaliacaoRepository;
import com.clinica.fisio.demo.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository repository;
    @Autowired
    private AvaliacaoService service;

    @PostMapping
    public Avaliacao salvar(@RequestBody Avaliacao avaliacao) {
        // Agora pegamos o ID de dentro do objeto paciente
        if (avaliacao.getPaciente() != null) {
            System.out.println("Salvando avaliação do paciente ID: " + avaliacao.getPaciente().getId());
        }
        // FALTAVA ESSA LINHA ABAIXO:
        return repository.save(avaliacao);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Avaliacao> buscarPorPaciente(@PathVariable Long pacienteId) {
        // Busca as avaliações filtrando pelo ID do objeto paciente
        return repository.findByPacienteId(pacienteId);
    }
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> baixarPdf(@PathVariable Long id) {
        Avaliacao avaliacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        byte[] pdf = service.gerarPdf(avaliacao);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "Relatorio_RTS_" + id + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
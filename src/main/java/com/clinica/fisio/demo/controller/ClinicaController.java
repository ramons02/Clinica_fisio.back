package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.dto.PacienteDTO;
import com.clinica.fisio.demo.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Will be centralized
public class ClinicaController {

    @Autowired
    private PacienteService pacienteService;

    // PACIENTES
    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @PostMapping(value = "/pacientes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> salvarPaciente(
            @RequestPart("paciente") PacienteDTO pacienteEntrada,
            @RequestPart(value = "exame", required = false) MultipartFile arquivo) {

        try {
            pacienteService.salvarPaciente(pacienteEntrada, arquivo);
            return ResponseEntity.ok(Map.of("success", true, "message", "Dados preservados e atualizados!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        try {
            pacienteService.deletarPaciente(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/pacientes/{id}")
    public ResponseEntity<PacienteDTO> atualizar(@PathVariable Long id, @RequestBody PacienteDTO paciente) {
        try {
            return ResponseEntity.ok(pacienteService.atualizar(id, paciente));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
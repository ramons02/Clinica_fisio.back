package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.dto.ExameDTO;
import com.clinica.fisio.demo.model.Exame;
import com.clinica.fisio.demo.service.ExameService;
import com.clinica.fisio.demo.service.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exames")
// CrossOrigin will be replaced by Global Config later, but keeping it for now to avoid breaking midway.
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "Content-Disposition")
public class ExameController {

    @Autowired
    private ExameService exameService;
    
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExameDTO> criar(
            @RequestPart("exame") String exameJson,
            @RequestPart("arquivo") MultipartFile arquivo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            ExameDTO exameDTO = mapper.readValue(exameJson, ExameDTO.class);

            if (arquivo == null || arquivo.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Abstracted storage logic
            String savedFileName = fileStorageService.storeFile(arquivo);
            exameDTO.setUrlArquivo(savedFileName);

            // Mapper to Entity (Since we don't have Exame mapping yet in Service)
            Exame exame = new Exame();
            exame.setTitulo(exameDTO.getTitulo());
            exame.setTipo(exameDTO.getTipo());
            exame.setDescricao(exameDTO.getDescricao());
            exame.setDataExame(exameDTO.getDataExame());
            exame.setUrlArquivo(exameDTO.getUrlArquivo());
            
            // For saving, ExameService expects Exame entity
            Exame salvo = exameService.salvarExame(exame); // Note: Paciente relationship needs review if it was passed

            exameDTO.setId(salvo.getId());
            return ResponseEntity.ok(exameDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ExameDTO>> listarPorPaciente(@PathVariable Long id) {
        List<Exame> exames = exameService.listarExamesPorPaciente(id);
        List<ExameDTO> dtos = exames.stream().map(e -> {
            ExameDTO dto = new ExameDTO();
            dto.setId(e.getId());
            dto.setTitulo(e.getTitulo());
            dto.setTipo(e.getTipo());
            dto.setDataExame(e.getDataExame());
            dto.setDescricao(e.getDescricao());
            dto.setUrlArquivo(e.getUrlArquivo());
            if (e.getPaciente() != null) dto.setPacienteId(e.getPaciente().getId());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Exame exame = exameService.buscarPorId(id);
        if (exame != null && exame.getUrlArquivo() != null) {
            fileStorageService.deleteFile(exame.getUrlArquivo());
        }
        exameService.deletarExame(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadExame(@PathVariable Long id) {
        try {
            Exame exame = exameService.buscarPorId(id);
            Resource resource = fileStorageService.loadFileAsResource(exame.getUrlArquivo());

            String contentType = "application/octet-stream"; // Default content type

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exame.getUrlArquivo() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
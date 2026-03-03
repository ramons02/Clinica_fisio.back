package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.model.Exame;
import com.clinica.fisio.demo.model.Pacientes;
import com.clinica.fisio.demo.service.ExameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/exames")
@CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "Content-Disposition")
public class ExameController {

    @Autowired
    private ExameService exameService;

    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

    public ExameController() {
        try {
            // Cria a pasta uploads automaticamente se não existir
            Files.createDirectories(root);
        } catch (IOException e) {
            System.err.println("Erro ao criar pasta de uploads: " + e.getMessage());
        }
    }

    // AJUSTADO: Agora recebe o arquivo e os dados do exame
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Exame> criar(
            @RequestPart("exame") String exameJson,
            @RequestPart("arquivo") MultipartFile arquivo) {
        try {
            // AJUSTE AQUI: Registrando o módulo de data no ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            Exame exame = mapper.readValue(exameJson, Exame.class);

            // Limpar o nome do arquivo para evitar o erro de ".docx.pdf"
            String nomeOriginal = arquivo.getOriginalFilename();
            if (nomeOriginal == null) return ResponseEntity.badRequest().build();

            // Salvar fisicamente
            Files.copy(arquivo.getInputStream(), this.root.resolve(nomeOriginal), StandardCopyOption.REPLACE_EXISTING);

            exame.setUrlArquivo(nomeOriginal);
            return ResponseEntity.ok(exameService.salvarExame(exame));

        } catch (Exception e) {
            e.printStackTrace(); // Isso vai nos mostrar se houver erro de permissão no Linux
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Exame>> listarPorPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(exameService.listarExamesPorPaciente(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Opcional: deletar o arquivo físico aqui também
        exameService.deletarExame(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadExame(@PathVariable Long id) {
        try {
            Exame exame = exameService.buscarPorId(id);
            Path arquivoPath = this.root.resolve(exame.getUrlArquivo()).normalize();

            System.out.println("DEBUG: Tentando ler o arquivo em: " + arquivoPath.toString());

            Resource resource = new UrlResource(arquivoPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Tenta descobrir o tipo do arquivo (PDF, Imagem, etc)
                String contentType = Files.probeContentType(arquivoPath);
                if (contentType == null) contentType = "application/octet-stream";

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exame.getUrlArquivo() + "\"")
                        .body(resource);
            } else {
                System.err.println("DEBUG: Arquivo não encontrado no caminho: " + arquivoPath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
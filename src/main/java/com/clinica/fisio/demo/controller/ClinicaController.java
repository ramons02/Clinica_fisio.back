package com.clinica.fisio.demo.controller;



import com.clinica.fisio.demo.model.Pacientes;
import com.clinica.fisio.demo.repository.AgendaRepository;

import com.clinica.fisio.demo.repository.PacienteRepository;
import com.clinica.fisio.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ClinicaController {

    @Autowired
    private UsuarioRepository userRepo;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private AgendaRepository agendaRepo;




    // PACIENTES
    @GetMapping("/pacientes")
    public List<Pacientes> listarPacientes() {
        return pacienteRepository.findAll();
    }



    /**
     * SALVAR PACIENTE COM EXAME
     * Atualizado para receber as "Partes" unificadas do Angular
     */
    @PostMapping(value = "/pacientes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> salvarPaciente(
            @RequestPart("paciente") Pacientes pacienteEntrada,
            @RequestPart(value = "exame", required = false) MultipartFile arquivo) {

        try {
            Pacientes pacienteParaSalvar;

            if (pacienteEntrada.getId() != null) {
                pacienteParaSalvar = pacienteRepository.findById(pacienteEntrada.getId())
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

                // --- LÓGICA DE PROTEÇÃO PARA TODOS OS CAMPOS ---

                // Dados Pessoais e Contato
                if (pacienteEntrada.getNome() != null && !pacienteEntrada.getNome().isEmpty()) {
                    pacienteParaSalvar.setNome(pacienteEntrada.getNome());
                }
                if (pacienteEntrada.getCpf() != null && !pacienteEntrada.getCpf().isEmpty()) {
                    pacienteParaSalvar.setCpf(pacienteEntrada.getCpf());
                }
                if (pacienteEntrada.getTelefone() != null && !pacienteEntrada.getTelefone().isEmpty()) {
                    pacienteParaSalvar.setTelefone(pacienteEntrada.getTelefone());
                }
                if (pacienteEntrada.getEndereco() != null && !pacienteEntrada.getEndereco().isEmpty()) {
                    pacienteParaSalvar.setEndereco(pacienteEntrada.getEndereco());
                }

                // Dados Físicos
                if (pacienteEntrada.getPeso() != null && !pacienteEntrada.getPeso().isEmpty()) {
                    pacienteParaSalvar.setPeso(pacienteEntrada.getPeso());
                }
                if (pacienteEntrada.getAltura() != null && !pacienteEntrada.getAltura().isEmpty()) {
                    pacienteParaSalvar.setAltura(pacienteEntrada.getAltura());
                }

                // Dados Clínicos da Reabilitação
                if (pacienteEntrada.getCondicao() != null && !pacienteEntrada.getCondicao().isEmpty()) {
                    pacienteParaSalvar.setCondicao(pacienteEntrada.getCondicao());
                }

                if (pacienteEntrada.getHistoricoLesao() != null && !pacienteEntrada.getHistoricoLesao().isEmpty()) {
                    pacienteParaSalvar.setHistoricoLesao(pacienteEntrada.getHistoricoLesao());
                }

                if (pacienteEntrada.getObjetivos() != null && !pacienteEntrada.getObjetivos().isEmpty()) {
                    pacienteParaSalvar.setObjetivos(pacienteEntrada.getObjetivos());
                }


                // Financeiro
                if (pacienteEntrada.getPlano() != null && !pacienteEntrada.getPlano().isEmpty()) {
                    pacienteParaSalvar.setPlano(pacienteEntrada.getPlano());
                }

                // +++ NOVA LÓGICA DE PAGAMENTO AQUI +++
                // Como é um Boolean, não usamos isEmpty(), apenas verificamos se não é nulo
                if (pacienteEntrada.getPagoEsteMes() != null) {
                    pacienteParaSalvar.setPagoEsteMes(pacienteEntrada.getPagoEsteMes());
                }

            } else {
                pacienteParaSalvar = pacienteEntrada;
            }

            // 3. Lógica do Arquivo (Exame) - Mantida conforme original
            if (arquivo != null && !arquivo.isEmpty()) {
                String diretorioDestino = "/home/ramon/Documentos/clinica_fisio/uploads/";
                File folder = new File(diretorioDestino);
                if (!folder.exists()) folder.mkdirs();

                String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Files.write(Paths.get(diretorioDestino + nomeArquivo), arquivo.getBytes());

                pacienteParaSalvar.setExameUrl(nomeArquivo);
            }

            pacienteRepository.save(pacienteParaSalvar);
            return ResponseEntity.ok(Map.of("success", true, "message", "Dados preservados e atualizados!"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    // REMOVER PACIENTE
    // Dentro do seu ClinicaController.java

    @DeleteMapping("/pacientes/{id}") // O caminho será /api/clinica/pacientes/ID
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        try {
            // Supondo que seu repository de pacientes esteja injetado aqui
            pacienteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
    // Ajuste o caminho para bater com o que o Angular envia
    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Pacientes> atualizar(@PathVariable Long id, @RequestBody Pacientes paciente) {
        return pacienteRepository.findById(id).map(p -> {
            // Atualiza apenas os campos que vieram do formulário
            p.setNome(paciente.getNome());
            p.setTelefone(paciente.getTelefone());
            p.setCondicao(paciente.getCondicao());
            // Se quiser editar mais campos, adicione aqui: p.setPeso(paciente.getPeso());

            Pacientes atualizado = pacienteRepository.save(p);
            return ResponseEntity.ok(atualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
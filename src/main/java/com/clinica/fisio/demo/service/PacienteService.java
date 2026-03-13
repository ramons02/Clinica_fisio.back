package com.clinica.fisio.demo.service;

import com.clinica.fisio.demo.dto.PacienteDTO;
import com.clinica.fisio.demo.model.Paciente;
import com.clinica.fisio.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PacienteDTO salvarPaciente(PacienteDTO pacienteDTO, MultipartFile arquivo) {
        Paciente paciente = new Paciente();

        if (pacienteDTO.getId() != null) {
            paciente = pacienteRepository.findById(pacienteDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + pacienteDTO.getId()));
        }

        // Dados Pessoais e Contato
        if (pacienteDTO.getNome() != null && !pacienteDTO.getNome().isEmpty()) paciente.setNome(pacienteDTO.getNome());
        if (pacienteDTO.getCpf() != null && !pacienteDTO.getCpf().isEmpty()) paciente.setCpf(pacienteDTO.getCpf());
        if (pacienteDTO.getTelefone() != null && !pacienteDTO.getTelefone().isEmpty()) paciente.setTelefone(pacienteDTO.getTelefone());
        if (pacienteDTO.getEndereco() != null && !pacienteDTO.getEndereco().isEmpty()) paciente.setEndereco(pacienteDTO.getEndereco());

        // Dados Físicos
        if (pacienteDTO.getPeso() != null && !pacienteDTO.getPeso().isEmpty()) paciente.setPeso(pacienteDTO.getPeso());
        if (pacienteDTO.getAltura() != null && !pacienteDTO.getAltura().isEmpty()) paciente.setAltura(pacienteDTO.getAltura());

        // Dados Clínicos da Reabilitação
        if (pacienteDTO.getCondicao() != null && !pacienteDTO.getCondicao().isEmpty()) paciente.setCondicao(pacienteDTO.getCondicao());
        if (pacienteDTO.getHistoricoLesao() != null && !pacienteDTO.getHistoricoLesao().isEmpty()) paciente.setHistoricoLesao(pacienteDTO.getHistoricoLesao());
        if (pacienteDTO.getObjetivos() != null && !pacienteDTO.getObjetivos().isEmpty()) paciente.setObjetivos(pacienteDTO.getObjetivos());

        // Financeiro
        if (pacienteDTO.getPlano() != null && !pacienteDTO.getPlano().isEmpty()) paciente.setPlano(pacienteDTO.getPlano());
        if (pacienteDTO.getPagoEsteMes() != null) paciente.setPagoEsteMes(pacienteDTO.getPagoEsteMes());

        // Arquivo
        if (arquivo != null && !arquivo.isEmpty()) {
            String fileName = fileStorageService.storeFile(arquivo);
            paciente.setExameUrl(fileName);
        }

        paciente = pacienteRepository.save(paciente);
        return convertToDTO(paciente);
    }

    public void deletarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }

    public PacienteDTO atualizar(Long id, PacienteDTO pacienteDTO) {
        return pacienteRepository.findById(id).map(p -> {
            p.setNome(pacienteDTO.getNome());
            p.setTelefone(pacienteDTO.getTelefone());
            p.setCondicao(pacienteDTO.getCondicao());
            
            Paciente atualizado = pacienteRepository.save(p);
            return convertToDTO(atualizado);
        }).orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + id));
    }

    public PacienteDTO convertToDTO(Paciente paciente) {
        if (paciente == null) return null;
        
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setCpf(paciente.getCpf());
        dto.setTelefone(paciente.getTelefone());
        dto.setPeso(paciente.getPeso());
        dto.setAltura(paciente.getAltura());
        dto.setEndereco(paciente.getEndereco());
        dto.setCondicao(paciente.getCondicao());
        dto.setPlano(paciente.getPlano());
        dto.setObjetivos(paciente.getObjetivos());
        dto.setHistoricoLesao(paciente.getHistoricoLesao());
        dto.setExameUrl(paciente.getExameUrl());
        dto.setPagoEsteMes(paciente.getPagoEsteMes());
        dto.setDataInicioPlano(paciente.getDataInicioPlano());
        return dto;
    }
}

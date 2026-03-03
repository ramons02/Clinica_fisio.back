package com.clinica.fisio.demo.service;

import com.clinica.fisio.demo.model.Exame;
import com.clinica.fisio.demo.repository.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    public Exame salvarExame(Exame exame) {
        return exameRepository.save(exame);
    }

    public List<Exame> listarExamesPorPaciente(Long pacienteId) {
        return exameRepository.findByPacienteId(pacienteId);
    }

    public void deletarExame(Long id) {
        exameRepository.deleteById(id);
    }

    // ADICIONE ESTE MÉTODO ABAIXO PARA O DOWNLOAD FUNCIONAR
    public Exame buscarPorId(Long id) {
        return exameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exame não encontrado com o ID: " + id));
    }
}
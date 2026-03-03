package com.clinica.fisio.demo.repository;

import com.clinica.fisio.demo.model.Evolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvolucaoRepository extends JpaRepository<Evolucao, Long> {
    List<Evolucao> findByPacienteId(Long pacienteId);
}
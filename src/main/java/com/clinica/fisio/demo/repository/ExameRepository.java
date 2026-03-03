package com.clinica.fisio.demo.repository;

import com.clinica.fisio.demo.model.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
    // Busca todos os exames de um paciente específico
    List<Exame> findByPacienteId(Long pacienteId);
}
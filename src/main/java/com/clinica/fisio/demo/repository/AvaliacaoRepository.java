package com.clinica.fisio.demo.repository;

import com.clinica.fisio.demo.model.Avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByPacienteId(Long pacienteId);
}

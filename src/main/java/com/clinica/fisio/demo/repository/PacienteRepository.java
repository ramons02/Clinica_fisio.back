package com.clinica.fisio.demo.repository;

import com.clinica.fisio.demo.model.Pacientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Pacientes, Long> {
}

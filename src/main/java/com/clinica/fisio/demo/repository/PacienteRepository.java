package com.clinica.fisio.demo.repository;

import com.clinica.fisio.demo.model.Pacientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Pacientes, Long> {
    // Não precisa escrever nada aqui para o delete funcionar!
    // O JpaRepository já tem o deleteById internamente.
}

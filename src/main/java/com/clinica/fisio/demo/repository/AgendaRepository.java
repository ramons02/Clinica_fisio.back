package com.clinica.fisio.demo.repository;

import com.clinica.fisio.demo.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}

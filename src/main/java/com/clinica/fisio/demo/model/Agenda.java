package com.clinica.fisio.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime start; // Nomeado como 'start' para o FullCalendar
    private String title;         // Nomeado como 'title' para o FullCalendar

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Pacientes paciente;
}

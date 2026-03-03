package com.clinica.fisio.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "evolucoes")
@Data
public class Evolucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id")
    private Long pacienteId;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro = LocalDateTime.now();
}
package com.clinica.fisio.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Garante que traga os dados do paciente
    @JoinColumn(name = "paciente_id", nullable = false)
    private Pacientes paciente;

    @Column(name = "data_avaliacao")
    private LocalDate dataAvaliacao;

    // Dinamometria
    private double extDir;
    private double extEsq;
    private double flexDir;
    private double flexEsq;

    // Hop Tests
    private double singleDir;
    private double singleEsq;
    private double tripleDir;
    private double tripleEsq;

    @Column(name = "medo_movimento")
    private Integer medoMovimento;

    // Método auxiliar para garantir que a data seja salva ao criar
    @PrePersist
    protected void onCreate() {
        if (this.dataAvaliacao == null) {
            this.dataAvaliacao = LocalDate.now();
        }
    }
}
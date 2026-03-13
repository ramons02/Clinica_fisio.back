package com.clinica.fisio.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "exames")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;      // Ex: Ressonância do Joelho
    private String tipo;        // Ex: Raio-X, Ultrassom, Laudo
    private LocalDate dataExame;

    @Column(columnDefinition = "TEXT")
    private String descricao;   // Detalhes sobre o que foi encontrado

    private String urlArquivo;  // O link ou caminho para o arquivo PDF/Imagem

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;  // Relaciona o exame ao dono (Paciente)
}
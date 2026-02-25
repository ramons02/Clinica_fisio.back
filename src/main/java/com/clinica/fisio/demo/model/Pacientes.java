package com.clinica.fisio.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@Table(name = "pacientes")
public class Pacientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String telefone;
    private String peso;
    private String altura;
    private String endereco;
    private String condicao;
    private String plano;

    @Column(columnDefinition = "TEXT")
    private String objetivos;

    @Column(name = "historico_lesao", columnDefinition = "TEXT")
    private String historicoLesao;

    @Column(columnDefinition = "TEXT")
    private String medicamentos;

    @Column(name = "queixa_principal", columnDefinition = "TEXT")
    private String queixaPrincipal;

    @Column(name = "observacoes_gerais", columnDefinition = "TEXT")
    private String observacoesGerais;

    // Coluna para o caminho do arquivo de exame
    private String exameUrl;

    @JsonProperty("pagoEsteMes")
    @Column(name = "pago_este_mes") // Isso liga o Java ao nome da sua tabela no banco
    private Boolean pagoEsteMes = false;

    private LocalDate dataInicioPlano;

    public boolean isVencimentoProximo() {
        if (dataInicioPlano == null) return false;

        LocalDate dataVencimento = dataInicioPlano.plusDays(30);
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), dataVencimento);

        // Retorna verdadeiro se faltar entre 0 e 5 dias
        return diasRestantes <= 5 && diasRestantes >= 0;
    }

}

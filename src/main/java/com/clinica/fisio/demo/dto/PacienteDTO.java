package com.clinica.fisio.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String peso;
    private String altura;
    private String endereco;
    private String condicao;
    private String plano;
    private String objetivos;
    private String historicoLesao;
    private String exameUrl;

    @JsonProperty("pagoEsteMes")
    private Boolean pagoEsteMes = false;

    private LocalDate dataInicioPlano;
}

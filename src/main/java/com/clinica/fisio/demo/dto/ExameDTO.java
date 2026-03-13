package com.clinica.fisio.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExameDTO {
    private Long id;
    private String titulo;
    private String tipo;
    private LocalDate dataExame;
    private String descricao;
    private String urlArquivo;
    private Long pacienteId;
}

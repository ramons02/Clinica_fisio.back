package com.clinica.fisio.demo.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String senha;
    private String nome;
    private String cargo;
}

package com.clinica.fisio.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracoes")
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double mensalPreOp;
    private Double mensalPosOp;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getMensalPreOp() { return mensalPreOp; }
    public void setMensalPreOp(Double mensalPreOp) { this.mensalPreOp = mensalPreOp; }
    public Double getMensalPosOp() { return mensalPosOp; }
    public void setMensalPosOp(Double mensalPosOp) { this.mensalPosOp = mensalPosOp; }
}

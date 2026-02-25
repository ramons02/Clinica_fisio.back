package com.clinica.fisio.demo.repository;


import com.clinica.fisio.demo.model.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
    // Aqui o Spring já cria tudo automático para você!
}
package com.clinica.fisio.demo.service;

import com.clinica.fisio.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return repository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado: " + username
                        ));
    }
}
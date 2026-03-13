package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.dto.UsuarioDTO;
import com.clinica.fisio.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String username = dados.get("username");
        String senhaDigitada = dados.get("senha");

        Map<String, String> authResult = usuarioService.login(username, senhaDigitada);

        if (authResult != null) {
            return ResponseEntity.ok(authResult);
        }

        return ResponseEntity.status(401).body("Erro");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioDTO usuario) {
        try {
            usuarioService.cadastrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}
package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.model.Usuarios;
import com.clinica.fisio.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {
        RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS
})
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public List<Usuarios> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Usuarios salvar(@RequestBody Usuarios usuarios) {
        if (usuarios.getCargo() != null) {
            usuarios.setCargo(usuarios.getCargo().toUpperCase());
        }
        return repository.save(usuarios);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String senha = loginData.get("senha");

        // Buscamos o usuário
        Optional<Usuarios> usuarioOpt = repository.findByUsername(username);

        // Verificamos se existe e se a senha bate
        if (usuarioOpt.isPresent() && usuarioOpt.get().getSenha().equals(senha)) {
            return ResponseEntity.ok(usuarioOpt.get()); // Sucesso: Retorna o objeto
        } else {
            // Erro: Retorna 401 com mensagem
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais Inválidas");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
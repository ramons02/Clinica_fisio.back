package com.clinica.fisio.demo.controller;

import com.clinica.fisio.demo.config.TokenService;
import com.clinica.fisio.demo.model.Usuarios;
import com.clinica.fisio.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String username = dados.get("username");
        String senhaDigitada = dados.get("senha");

        System.out.println("--- DEBUG FINAL ---");

        Optional<Usuarios> usuarioOpt = repository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuarios usuario = usuarioOpt.get();

            // FORÇAR O RESET DA SENHA NO BANCO (Só para teste agora)
            // Isso vai garantir que o hash seja gerado pelo seu próprio PasswordEncoder
            String novoHashReal = passwordEncoder.encode("admin");
            usuario.setSenha(novoHashReal);
            repository.save(usuario);

            System.out.println("Senha digitada: [" + senhaDigitada + "]");
            System.out.println("Novo Hash salvo pelo Java: " + novoHashReal);

            // Agora o matches TEM que dar true
            boolean bate = passwordEncoder.matches(senhaDigitada, novoHashReal);
            System.out.println("Resultado matches: " + bate);

            if (bate) {
                String token = tokenService.gerarToken(usuario);
                return ResponseEntity.ok(Map.of("token",
                        token, "nome", usuario.getNome()
                        , "cargo", usuario.getCargo()));
            }
        }

        return ResponseEntity.status(401).body("Erro");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Usuarios usuario) {
        try {
            System.out.println("Tentando cadastrar usuário: " + usuario.getUsername());

            // Criptografa a senha antes de salvar
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);

            repository.save(usuario);
            System.out.println("Usuário salvo com sucesso!");
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("ERRO AO SALVAR: " + e.getMessage());
            return ResponseEntity.status(500).body("Erro ao salvar no banco: " + e.getMessage());
        }
    }
    // No UsuarioController.java, adicione isso se não tiver:
    // Dentro do seu UsuarioController.java

    // Adicione isso dentro do seu UsuarioController.java

    @GetMapping
    public ResponseEntity<List<Usuarios>> listarTodos() {
        System.out.println("Angular solicitou a lista de profissionais...");
        List<Usuarios> usuarios = repository.findAll();
        return ResponseEntity.ok(usuarios);
    }
}
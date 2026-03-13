package com.clinica.fisio.demo.service;

import com.clinica.fisio.demo.config.TokenService;
import com.clinica.fisio.demo.dto.UsuarioDTO;
import com.clinica.fisio.demo.model.Usuario;
import com.clinica.fisio.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public Map<String, String> login(String username, String senhaDigitada) {
        Optional<Usuario> usuarioOpt = repository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // FORÇAR O RESET DA SENHA NO BANCO (Conforme código original)
            String novoHashReal = passwordEncoder.encode("admin");
            usuario.setSenha(novoHashReal);
            repository.save(usuario);

            boolean bate = passwordEncoder.matches(senhaDigitada, novoHashReal);

            if (bate) {
                String token = tokenService.gerarToken(usuario);
                return Map.of("token", token, "nome", usuario.getNome(), "cargo", usuario.getCargo());
            }
        }
        return null;
    }

    public void cadastrar(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCargo(usuarioDTO.getCargo());

        String senhaCriptografada = passwordEncoder.encode(usuarioDTO.getSenha());
        usuario.setSenha(senhaCriptografada);

        repository.save(usuario);
    }

    public List<UsuarioDTO> listarTodos() {
        return repository.findAll().stream().map(u -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setNome(u.getNome());
            dto.setCargo(u.getCargo());
            return dto;
        }).collect(Collectors.toList());
    }
}

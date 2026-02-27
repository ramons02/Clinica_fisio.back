package com.clinica.fisio.demo.config;

import com.clinica.fisio.demo.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Libera caminhos públicos e Preflight (CORS)
        String path = request.getServletPath();
        if (path.equals("/api/auth/login") || path.equals("/api/auth/cadastrar") ||
                request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Tenta recuperar o token
        String token = recoverToken(request);

        if (token != null) {
            // 3. Valida o token e pega o login (username)
            String login = tokenService.validarToken(token);

            if (login != null) {
                // 4. Busca o usuário no banco (que agora retorna Optional)
                var usuarioOptional = usuarioRepository.findByUsername(login);

                if (usuarioOptional.isPresent()) {
                    var usuario = usuarioOptional.get();
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );
                    // 5. Autentica o usuário no contexto do Spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 6. Continua a execução da requisição (ESSENCIAL!)
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
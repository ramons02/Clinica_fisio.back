package com.clinica.fisio.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.clinica.fisio.demo.model.Usuarios;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Essa chave vem do seu application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuarios usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("clinica-fisio-api") // Identificador da sua API
                    .withSubject(usuario.getUsername()) // Guarda o username no token
                    .withExpiresAt(genExpirationDate()) // Define quando o token vence
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("clinica-fisio-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return ""; // Se o token for inválido, retorna vazio
        }
    }

    // O token vai durar 2 horas (bom para segurança da clínica)
    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

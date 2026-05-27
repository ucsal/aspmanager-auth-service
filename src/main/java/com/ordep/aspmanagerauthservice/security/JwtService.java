package com.ordep.aspmanagerauthservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ordep.aspmanagerauthservice.dto.UsuarioAuthResponse;
import com.ordep.aspmanagerauthservice.enums.Perfil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(UsuarioAuthResponse usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("idUsuario", usuario.id())
                .withClaim("perfil", usuario.perfil().name())
                .withSubject(usuario.email())
                .withExpiresAt(Instant.now().plusSeconds(84600))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<UsuarioAuthResponse> validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build().verify(token);
        return Optional.of(new UsuarioAuthResponse(decodedJWT.getClaim("idUsuario").asLong(), null, null, Perfil.valueOf(decodedJWT.getClaim("perfil").asString()), null));
    }

}

package com.ordep.aspmanagerauthservice.controller;

import com.ordep.aspmanagerauthservice.client.UsuarioClient;
import com.ordep.aspmanagerauthservice.dto.LoginRequest;
import com.ordep.aspmanagerauthservice.dto.TokenResponse;
import com.ordep.aspmanagerauthservice.dto.UsuarioAuthResponse;
import com.ordep.aspmanagerauthservice.enums.StatusRegistro;
import com.ordep.aspmanagerauthservice.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações de autenticação e emissão de token JWT")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioClient usuarioClient;
    private final PasswordEncoder codificadorDeSenha;

    public AuthController(JwtService jwtService, UsuarioClient usuarioClient) {
        this.jwtService = jwtService;
        this.usuarioClient = usuarioClient;
        this.codificadorDeSenha = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    @Operation(operationId = "loginUsuario", summary = "Autenticar usuário", description = "Valida credenciais de acesso e retorna um token JWT para uso nas demais rotas protegidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirements
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {
        UsuarioAuthResponse usuario = usuarioClient.buscarPorEmail(request.email());

        if (!codificadorDeSenha.matches(request.senha(), usuario.senhaCriptografada())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        if (usuario.statusRegistro() != StatusRegistro.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário inativo");
        }

        String token = jwtService.gerarToken(usuario);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @GetMapping("/validate")
    public ResponseEntity<UsuarioAuthResponse> validate(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring("Bearer ".length());
        return jwtService.validarToken(token)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}

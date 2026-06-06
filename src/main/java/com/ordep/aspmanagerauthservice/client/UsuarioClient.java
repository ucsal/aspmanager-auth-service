package com.ordep.aspmanagerauthservice.client;

import com.ordep.aspmanagerauthservice.dto.UsuarioAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ASPMANAGER-USUARIO-SERVICE")
public interface UsuarioClient {
    @GetMapping("/api/v1/usuarios/email/{email}")
    UsuarioAuthResponse buscarPorEmail(@PathVariable String email);
}
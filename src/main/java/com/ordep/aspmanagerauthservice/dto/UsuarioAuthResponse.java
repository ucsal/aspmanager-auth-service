package com.ordep.aspmanagerauthservice.dto;

import com.ordep.aspmanagerauthservice.enums.Perfil;
import com.ordep.aspmanagerauthservice.enums.StatusRegistro;

public record UsuarioAuthResponse(
        Long id,
        String email,
        String senhaCriptografada,
        Perfil perfil,
        StatusRegistro statusRegistro) {
}

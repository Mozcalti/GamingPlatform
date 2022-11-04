package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.CredencialesUsuarioDTO;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.security.jwt.JwtUtil;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    @Value("${security.jwt.token.prefix}")
    private String prefix;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> getToken(@Valid @RequestBody CredencialesUsuarioDTO credenciales){

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credenciales.username(), credenciales.password()));

        String jwt = jwtUtil.getToken(auth.getName());

        Optional<Usuario> maybeUsuario = usuarioService.getUsuario(auth.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, String.format("%s%s", prefix, jwt))
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                .body(new UsuarioDTO(maybeUsuario.orElseThrow()));
    }
}

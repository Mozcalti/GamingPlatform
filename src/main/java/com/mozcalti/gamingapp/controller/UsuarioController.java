package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RolesAllowed("ROLE_STAFF")
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @PostMapping
    public ResponseEntity<Void> addUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

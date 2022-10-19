package com.mozcalti.gamingapp.service.usuarios.impl;

import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.repository.UsuarioRepository;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public void save(UsuarioDTO usuarioDTO) {
        usuarioRepository.save(new Usuario(usuarioDTO));
    }

    @Override
    public List<UsuarioDTO> getAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream().map(UsuarioDTO::new).toList();
    }
}

package com.mozcalti.gamingapp.service.usuarios.impl;

import com.mozcalti.gamingapp.events.user.OnUsuarioRegistradoEvent;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;
import com.mozcalti.gamingapp.repository.UsuarioRepository;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void save(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByEmail(usuarioDTO.email()).isPresent()){
            throw new ValidacionException("El usuario %s ya existe en la base de datos".formatted(usuarioDTO.email()));
        }

        eventPublisher.publishEvent(new OnUsuarioRegistradoEvent(usuarioRepository.save(new Usuario(usuarioDTO))));
    }

    @Override
    public Usuario update(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


    @Override
    public List<UsuarioDTO> getAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream().map(UsuarioDTO::new).toList();
    }
}

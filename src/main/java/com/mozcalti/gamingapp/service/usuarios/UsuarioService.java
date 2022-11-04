package com.mozcalti.gamingapp.service.usuarios;


import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.model.dto.UsuarioDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    void save(UsuarioDTO usuarioDTO);

    Optional<Usuario> getUsuario(String email);

    Usuario update(Usuario usuario);

    List<UsuarioDTO> getAll();
}

package com.mozcalti.gamingapp.service.usuarios;


import com.mozcalti.gamingapp.model.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    void save(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> getAll();
}

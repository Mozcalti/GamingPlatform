package com.mozcalti.gamingapp.events.user;

import com.mozcalti.gamingapp.model.Usuario;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnUsuarioRegistradoEvent extends ApplicationEvent {

    private final Usuario usuario;
    public OnUsuarioRegistradoEvent(Usuario usuario) {
        super(usuario);
        this.usuario = usuario;
    }
}

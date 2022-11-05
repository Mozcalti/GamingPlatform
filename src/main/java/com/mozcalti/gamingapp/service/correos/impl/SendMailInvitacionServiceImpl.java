package com.mozcalti.gamingapp.service.correos.impl;

import com.mozcalti.gamingapp.service.correos.SendMailInvitacionSevice;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.Participantes;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class SendMailInvitacionServiceImpl implements SendMailInvitacionSevice {
    private static final String MAIL_TEMPLATE = "invitacion";
    private SendMailService sendMailService;
    @SneakyThrows
    @Override
    public void mailsInvitacion(Participantes participantes) throws UtilsException {

        String mailTo = participantes.getCorreo();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nombre" , participantes.getNombre());
        parameters.put("apellido" , participantes.getApellidos());
        sendMailService.sendMail(mailTo, MAIL_TEMPLATE, parameters);
        log.info("Se envio el email de bienvenida a los participantes");
    }
}


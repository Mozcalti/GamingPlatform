package com.mozcalti.gamingapp.listeners.user;

import com.mozcalti.gamingapp.events.user.OnUsuarioRegistradoEvent;
import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.service.usuarios.VerificationTokenService;
import com.mozcalti.gamingapp.utils.ServerUtils;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnUsuarioRegistradoEvent> {

    private static final String MAIL_TEMPLATE_KEY = "registro";

    private final SendMailService sendMailService;
    private final VerificationTokenService verificationTokenService;

    @Override
    public void onApplicationEvent(OnUsuarioRegistradoEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnUsuarioRegistradoEvent event) {
        Usuario usuario = event.getUsuario();
        String token = UUID.randomUUID().toString();
        verificationTokenService.addVerificationToken(usuario, token);

        final String baseUrl = ServerUtils.getBaseUrl();

        String mailTo = usuario.getEmail();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("token", token);
        parameters.put("baseUrl", baseUrl);
        parameters.put("nombre", usuario.getNombre());

        try {
            sendMailService.sendMail(mailTo, MAIL_TEMPLATE_KEY, parameters);
        } catch (SendMailException | UtilsException e) {
            log.error("Error al enviar el correo de activacion de cuenta: {}", StackTraceUtils.getCustomStackTrace(e));
        }
    }


}

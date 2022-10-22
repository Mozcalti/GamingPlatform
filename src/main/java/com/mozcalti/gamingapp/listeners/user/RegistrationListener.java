package com.mozcalti.gamingapp.listeners.user;

import com.mozcalti.gamingapp.events.user.OnUsuarioRegistradoEvent;
import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.Usuario;
import com.mozcalti.gamingapp.service.SendMailService;
import com.mozcalti.gamingapp.service.usuarios.VerificationTokenService;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnUsuarioRegistradoEvent> {

    private final SendMailService sendMailService;
    private final VerificationTokenService verificationTokenService;

    @Override
    public void onApplicationEvent(OnUsuarioRegistradoEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnUsuarioRegistradoEvent event) {
        Usuario usuario = event.getUsuario();
        String token = UUID.randomUUID().toString();

        log.debug("token {}", token);
        verificationTokenService.addVerificationToken(usuario, token);

        final String baseUrl = getBaseUrl();

        log.debug("baseUrl: {}", baseUrl);


        String mailTo = usuario.getEmail();
        String subject = "Registro confirmado";
        String mailTemplate = "/template/mail/Registro.html";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("token", token);
        parameters.put("baseUrl", baseUrl);

        log.debug("token: {}", token);
        log.debug("baseUrl: {}", baseUrl);

        try {
            String templateMessage = sendMailService.readMailTemplate(mailTemplate, parameters);

            Map<String, String> imagesMessage = new HashMap<>();
            imagesMessage.put("logo_plai", "/img/logo_plai.png");

            sendMailService.sendMail(mailTo, subject, templateMessage, imagesMessage);
        } catch (SendMailException | UtilsException e) {
            log.error("Error en el job mailInicioBatallas(): {}", StackTraceUtils.getCustomStackTrace(e));
        }
    }

    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .build()
                .toUriString();
    }
}

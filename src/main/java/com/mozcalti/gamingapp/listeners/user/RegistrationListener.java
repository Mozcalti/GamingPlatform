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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnUsuarioRegistradoEvent> {

    private static final String MAIL_PROPERTIES_KEY = "registro";

    private final SendMailService sendMailService;
    private final VerificationTokenService verificationTokenService;

    private final MessageSource mailMessageSource;

    @Override
    public void onApplicationEvent(OnUsuarioRegistradoEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnUsuarioRegistradoEvent event) {
        Usuario usuario = event.getUsuario();
        String token = UUID.randomUUID().toString();
        verificationTokenService.addVerificationToken(usuario, token);

        final String baseUrl = getBaseUrl();

        log.debug("baseUrl: {}", baseUrl);

        String mailTo = usuario.getEmail();

        String subject = mailMessageSource.getMessage("%s.subject".formatted(MAIL_PROPERTIES_KEY), null, "Registro confirmado",null);
        String mailTemplate = mailMessageSource.getMessage("%s.template".formatted(MAIL_PROPERTIES_KEY), null, "/template/mail/Registro.html",null);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("token", token);
        parameters.put("baseUrl", baseUrl);
        parameters.put("nombre", usuario.getNombre());

        try {
            String templateMessage = sendMailService.readMailTemplate(mailTemplate, parameters);

            Map<String, String> imagesMessage = new HashMap<>();
            imagesMessage.put("logo_plai", "/img/logo_plai.png");

            sendMailService.sendMail(mailTo, subject, templateMessage, imagesMessage);
        } catch (SendMailException | UtilsException e) {
            log.error("Error al enviar el correo de activacion de cuenta: {}", StackTraceUtils.getCustomStackTrace(e));
        }
    }

    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .build()
                .toUriString();
    }
}

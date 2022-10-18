package com.mozcalti.gamingapp.jobs;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.service.*;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SendMailTorneo {

    private SendMailService sendMailService;

    private CalendarizarEtapasTorneoService calendarizarEtapasTorneoService;

    @Scheduled(cron = "${cron.mail.batallas}")
    public void mailInicioBatallas() throws UtilsException {

        log.info("Se buscan correos para el envio a los participantes");

        try {

            List<DatosCorreoBatallaDTO> mailsbatallas = calendarizarEtapasTorneoService.getDatosCorreoBatalla();

            if(!mailsbatallas.isEmpty()) {
                for(DatosCorreoBatallaDTO datosCorreoBatallaDTO : mailsbatallas) {
                    String mailTo = datosCorreoBatallaDTO.getMailToParticipantes();
                    String subject = "Inicio de Batalla";
                    String mailTemplate = "/template/mail/Mensaje.html";
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("mailBatalla", datosCorreoBatallaDTO);

                    String templateMessage = sendMailService.readMailTemplate(mailTemplate, parameters);

                    Map<String, String> imagesMessage = new HashMap<>();
                    imagesMessage.put("logo_plai", "/img/logo_plai.png");

                    sendMailService.sendMail(mailTo, subject, templateMessage, imagesMessage);

                    log.info("Se hace el envio de email a los participantes indicados");
                }
            } else {
                log.info("No se encontraron correos a enviar");
            }

        } catch (Exception e) {
            log.error("Error en el job mailInicioBatallas(): {}", StackTraceUtils.getCustomStackTrace(e));
        }

    }

}

package com.mozcalti.gamingapp.jobs;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.service.torneo.TorneosService;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class SendMailTorneo {

    private static final String MAIL_TEMPLATE_KEY = "torneo";

    public static final String ID_PARAMS_TEMPLATE_MAILS = "mailBatalla";

    private SendMailService sendMailService;

    private TorneosService torneosService;

    @Scheduled(cron = "${cron.mail-batallas}")
    public void mailInicioBatallas() throws UtilsException {

        log.info("Se buscan correos para el envio a los participantes");

        try {
            List<DatosCorreoBatallaDTO> mailsbatallas = torneosService.getDatosCorreoBatalla();

            if(!mailsbatallas.isEmpty()) {
                for(DatosCorreoBatallaDTO datosCorreoBatallaDTO : mailsbatallas) {
                    String mailTo = datosCorreoBatallaDTO.getMailToParticipantes();
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put(ID_PARAMS_TEMPLATE_MAILS, datosCorreoBatallaDTO);
                    sendMailService.sendMail(mailTo, MAIL_TEMPLATE_KEY, parameters);
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

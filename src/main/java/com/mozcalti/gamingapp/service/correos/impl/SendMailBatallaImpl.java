package com.mozcalti.gamingapp.service.correos.impl;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.service.correos.SendMailBatalla;
import com.mozcalti.gamingapp.service.correos.SendMailService;
import com.mozcalti.gamingapp.service.torneo.TorneosService;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SendMailBatallaImpl implements SendMailBatalla {

    public static final String MAIL_TEMPLATE_KEY = "torneo";

    public static final String ID_PARAMS_TEMPLATE_MAILS = "mailBatalla";

    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private TorneosService torneosService;

    @Override
    public void mailInicioBatallas() throws UtilsException {

        log.info("Se buscan correos para el envio a los participantes");

        try {
            List<DatosCorreoBatallaDTO> mailsbatallas = torneosService.getDatosCorreoBatalla();

            if(!mailsbatallas.isEmpty()) {
                int countMails = 0;
                for(DatosCorreoBatallaDTO datosCorreoBatallaDTO : mailsbatallas) {
                    String mailTo = datosCorreoBatallaDTO.getMailToParticipantes();
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put(ID_PARAMS_TEMPLATE_MAILS, datosCorreoBatallaDTO);

                    if(countMails == 50) {
                        log.info("... Haciendo un delay");
                        TimeUnit.MINUTES.sleep(30);
                        countMails = 0;
                    }

                    sendMailService.sendMail(mailTo, MAIL_TEMPLATE_KEY, parameters);
                    countMails+=datosCorreoBatallaDTO.getParticipantes().size();

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

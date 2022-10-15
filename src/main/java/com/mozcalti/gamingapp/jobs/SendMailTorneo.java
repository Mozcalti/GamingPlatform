package com.mozcalti.gamingapp.jobs;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.service.*;
import com.mozcalti.gamingapp.utils.StackTraceUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SendMailTorneo {
    private SendMailService sendMailService;

    private EtapaBatallaService etapaBatallaService;

    private BatallasService batallasService;

    private EquiposService equiposService;

    private BatallaParticipantesService batallaParticipantesService;

    private ParticipantesService participantesService;

    //@Scheduled(cron = "${cron.mail.batallas}")
    public void inicioBatallas() throws UtilsException {
        System.out.println("---------------> Se envian correos a los participantes");

        try {

            List<DatosCorreoBatallaDTO> mailsbatallas = new ArrayList<>();
            DatosCorreoBatallaDTO mailBatallasDTO;
            List<String> participante;
            Batallas batallas;
            Equipos equipos;
            for(EtapaBatalla etapaBatalla : etapaBatallaService.getAll()) {
                //System.out.println("-----> getIdEtapaBatalla: " + etapaBatallaEntity.getIdEtapaBatalla());
                //System.out.println("----> " + etapaBatallaEntity.getIdBatalla());

                batallas = batallasService.get(etapaBatalla.getIdBatalla());
                //System.out.println("----> " + batallasEntity.getFecha());
                //System.out.println("----> " + batallasEntity.getHoraInicio());
                //System.out.println("----> " + batallasEntity.getHoraFin());
                //System.out.println("----> " + batallasEntity.getBatallaParticipantesByIdBatalla().size());

                mailBatallasDTO = new DatosCorreoBatallaDTO(batallas.getFecha(),
                        batallas.getHoraInicio(), batallas.getHoraFin(),
                        batallas.getRondas());

                participante = new ArrayList<>();
                for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {

                    participante.add(batallaParticipantes.getNombre());


                    equipos = equiposService.get(batallaParticipantes.getIdParticipanteEquipo());

                    //System.out.println("---> " + equiposEntity.getParticipanteEquiposByIdEquipo().size());

                    for(ParticipanteEquipo participanteEquipo : equipos.getParticipanteEquiposByIdEquipo()) {
                        System.out.println("---> correo: " + participantesService.get(participanteEquipo.getIdParticipante()).getCorreo());
                    }
                }
                mailBatallasDTO.setParticipantes(participante);
                mailsbatallas.add(mailBatallasDTO);
            }

            for(DatosCorreoBatallaDTO datosCorreoBatallaDTO : mailsbatallas) {
                String mailTo = "juceruta2@gmail.com";
                String subject = "Buenas noches";
                String pathnameTemplate = "/template/mail/Mensaje.html";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("mailBatalla", datosCorreoBatallaDTO);

                String templateMessage = sendMailService.readFileTemplate(pathnameTemplate, parameters);

                Map<String, String> imagesMessage = new HashMap<>();
                imagesMessage.put("logoSpring", "/img/spring.png");
                imagesMessage.put("logo_plai", "/img/logo_plai.png");

                sendMailService.sendMail(mailTo, subject, templateMessage, imagesMessage);

                System.out.println("Email sent.");
            }

        } catch (Exception e) {
            System.out.println("Error en el job inicioBatallas():\n"
                    + StackTraceUtils.getCustomStackTrace(e));
        }
    }

}

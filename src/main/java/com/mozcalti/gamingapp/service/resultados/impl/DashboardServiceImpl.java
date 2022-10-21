package com.mozcalti.gamingapp.service.resultados.impl;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.model.batallas.resultado.RecordInfo;
import com.mozcalti.gamingapp.model.batallas.resultado.Result;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.repository.ResultadosRepository;
import com.mozcalti.gamingapp.repository.RobotsRepository;
import com.mozcalti.gamingapp.service.BatallasService;
import com.mozcalti.gamingapp.service.resultados.DashboardService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.FileUtils;
import com.mozcalti.gamingapp.validations.DashboardsGlobalResultadosValidation;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private BatallasService batallasService;

    @Autowired
    private EquiposRepository equiposRepository;

    @Autowired
    private RobotsRepository robotsRepository;

    @Autowired
    private ResultadosRepository resultadosRepository;

    @Value("${resources.static.resultados-batalla}")
    private String pathResultadosBatalla;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void buscaSalidaBatallas() {

        StringBuilder horaInicioBatalla = new StringBuilder();
        StringBuilder horaFinBatalla = new StringBuilder();
        StringBuilder fileResultadoBatallas = new StringBuilder();

        for(Batallas batallas : batallasService.getAll()) {

            horaInicioBatalla.delete(0, horaInicioBatalla.length());
            horaInicioBatalla.append(batallas.getFecha()).append(" ").append(batallas.getHoraInicio());

            horaFinBatalla.delete(0, horaFinBatalla.length());
            horaFinBatalla.append(batallas.getFecha()).append(" ").append(batallas.getHoraFin());

            try {
                DashboardsGlobalResultadosValidation.validaBatalla(batallas);
                DashboardsGlobalResultadosValidation.validaFechaHoraBatalla(horaInicioBatalla.toString(), horaFinBatalla.toString());

                for(BatallaParticipantes batallaParticipantes : batallas.getBatallaParticipantesByIdBatalla()) {
                    Optional<Equipos> equipos = equiposRepository.findById(batallaParticipantes.getIdParticipanteEquipo());

                    Optional<Robots> robot = robotsRepository.findAllByIdEquipo(equipos.orElseThrow().getIdEquipo()).stream()
                            .filter(r -> r.getActivo().equals(1))
                            .findFirst();

                    if(robot.isPresent()) {
                        fileResultadoBatallas.delete(0, fileResultadoBatallas.length());
                        fileResultadoBatallas.append(pathResultadosBatalla).append("/").append(robot.get().getIdRobot()).append(".xml");

                        DashboardsGlobalResultadosValidation.validaExisteArchivoXML(fileResultadoBatallas.toString());

                        StringBuilder lineas = FileUtils.getRecordInfo(fileResultadoBatallas.toString(),
                                Constantes.RECORD_INFO_INICIO,
                                Constantes.RECORD_INFO_FIN);

                        XStream xStream = new XStream();
                        xStream.addPermission(NoTypePermission.NONE);
                        xStream.addPermission(NullPermission.NULL);
                        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);

                        xStream.allowTypesByWildcard(new String[] {
                                "com.mozcalti.gamingapp.model.batallas.resultado.**"
                        });

                        xStream.processAnnotations(RecordInfo.class);

                        RecordInfo recordInfo = (RecordInfo) xStream.fromXML(lineas.toString());

                        for(Result result : recordInfo.getResults()) {
                            resultadosRepository.save(new Resultados(result, batallas.getIdBatalla()));
                        }

                        batallas.setBndTermina(1);
                    }
                }
                batallasService.save(batallas);
            } catch (ValidacionException | UtilsException e) {
                log.info(e.getMessage());
            }
        }

    }

    @Override
    public TablaDTO<ResultadosDTO> listaResultadosBatalla(String cadena, Integer indice) {
        return null;
    }

}

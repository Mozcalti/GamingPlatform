package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.EtapasEntity;
import com.mozcalti.gamingapp.model.TorneosEntity;
import com.mozcalti.gamingapp.repository.EtapasDao;
import com.mozcalti.gamingapp.repository.TorneosDao;
import com.mozcalti.gamingapp.request.EtapaRequest;
import com.mozcalti.gamingapp.request.TorneoRequest;
import com.mozcalti.gamingapp.service.TorneosService;
import com.mozcalti.gamingapp.utils.Constantes;
import com.mozcalti.gamingapp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TorneosServiceImpl extends GenericServiceImpl<TorneosEntity, Integer> implements TorneosService {

    @Autowired
    private TorneosDao torneosDao;
    @Autowired
    private EtapasDao etapasDao;

    @Override
    public CrudRepository<TorneosEntity, Integer> getDao() {
        return torneosDao;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveTorneo(TorneoRequest torneoRequest) throws ValidacionException {

        DateUtils.isValidDate(torneoRequest.getFechaInicio(),
                Constantes.FECHA_PATTERN,
                "Fecha de inicio del torneo no válido");

        DateUtils.isValidDate(torneoRequest.getFechaFin(),
                Constantes.FECHA_PATTERN,
                "Fecha de fin del torneo no válido");

        TorneosEntity torneosEntity = new TorneosEntity(torneoRequest);
        torneosEntity = torneosDao.save(torneosEntity);

        EtapasEntity etapasEntity = null;
        for (EtapaRequest etapaRequest : torneoRequest.getEtapasRequest()) {
            DateUtils.isValidDate(etapaRequest.getFechaInicio(),
                    Constantes.FECHA_PATTERN,
                    "Fecha de inicio de la etapa no válido");

            DateUtils.isValidDate(etapaRequest.getFechaFin(),
                    Constantes.FECHA_PATTERN,
                    "Fecha de fin de la etapa no válido");

            etapasEntity = new EtapasEntity(etapaRequest, torneosEntity);
            etapasDao.save(etapasEntity);
        }

    }
}

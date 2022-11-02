package com.mozcalti.gamingapp.service.torneo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.service.torneo.EtapasService;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.repository.EtapasRepository;
import com.mozcalti.gamingapp.service.dashboard.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EtapasServiceImpl extends GenericServiceImpl<Etapas, Integer> implements EtapasService {

    private EtapasRepository etapasRepository;

    private DashboardService dashboardService;

    @Override
    public CrudRepository<Etapas, Integer> getDao() {
        return etapasRepository;
    }

    @Override
    public List<ResultadosInstitucionGpoDTO> obtieneParticipantes(Integer idEtapa) {
        return dashboardService.gruopResultadosParticipantesBatalla(idEtapa);
    }
}

package com.mozcalti.gamingapp.service.equipo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.EtapaEquipo;
import com.mozcalti.gamingapp.repository.EtapaEquipoRepository;
import com.mozcalti.gamingapp.repository.ParticipanteEquipoRepository;
import com.mozcalti.gamingapp.service.equipo.EtapaEquipoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EtapaEquipoServiceImpl extends GenericServiceImpl<EtapaEquipo, Integer> implements EtapaEquipoService {

    private EtapaEquipoRepository etapaEquipoRepository;
    private ParticipanteEquipoRepository participanteEquipoRepository;
    
    @Override
    public CrudRepository<EtapaEquipo, Integer> getDao() {
        return etapaEquipoRepository;
    }

    @Override
    public Integer getEtapaPorEquipo(Integer idParticipante) {
        return etapaEquipoRepository.findByIdEquipo(participanteEquipoRepository.findByIdParticipante(idParticipante).getIdEquipo()).getIdEtapa();
    }
}

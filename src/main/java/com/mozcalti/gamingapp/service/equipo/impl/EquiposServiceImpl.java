package com.mozcalti.gamingapp.service.equipo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Equipos;
import com.mozcalti.gamingapp.model.EtapaEquipo;
import com.mozcalti.gamingapp.model.ParticipanteEquipo;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.repository.EtapaEquipoRepository;
import com.mozcalti.gamingapp.repository.ParticipanteEquipoRepository;
import com.mozcalti.gamingapp.service.equipo.EquiposService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EquiposServiceImpl extends GenericServiceImpl<Equipos, Integer> implements EquiposService {

    private EquiposRepository equiposRepository;

    private ParticipanteEquipoRepository participanteEquipoRepository;

    private EtapaEquipoRepository etapaEquipoRepository;

    @Override
    public CrudRepository<Equipos, Integer> getDao() {
        return equiposRepository;
    }

    @Override
    public Integer getIdEquipoByIdParticipante(Integer idEtapas, Integer idParticipante) throws ValidacionException {

        List<ParticipanteEquipo> participanteEquipos =
                participanteEquipoRepository.findAllByIdParticipante(idParticipante);

        Integer idEquipo = null;
        EtapaEquipo etapaEquipo;
        for(ParticipanteEquipo participanteEquipo : participanteEquipos) {
            etapaEquipo = etapaEquipoRepository
                    .findByIdEquipoAndIdEtapa(participanteEquipo.getIdEquipo(), idEtapas);

            if(etapaEquipo != null) {
                idEquipo = etapaEquipo.getIdEquipo();
            }
        }

        return idEquipo;

    }

}

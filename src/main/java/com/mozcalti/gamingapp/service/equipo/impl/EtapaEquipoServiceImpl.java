package com.mozcalti.gamingapp.service.equipo.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Equipos;
import com.mozcalti.gamingapp.model.EtapaEquipo;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.repository.EtapaEquipoRepository;
import com.mozcalti.gamingapp.repository.ParticipanteEquipoRepository;
import com.mozcalti.gamingapp.service.equipo.EtapaEquipoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EtapaEquipoServiceImpl extends GenericServiceImpl<EtapaEquipo, Integer> implements EtapaEquipoService {

    private EtapaEquipoRepository etapaEquipoRepository;
    private ParticipanteEquipoRepository participanteEquipoRepository;
    private EquiposRepository equiposRepository;
    
    @Override
    public CrudRepository<EtapaEquipo, Integer> getDao() {
        return etapaEquipoRepository;
    }

    @Override
    public Integer getEtapaPorEquipo(Integer idParticipante) {

        AtomicReference<Integer> idEtapa = new AtomicReference<>();

        participanteEquipoRepository.findAllByIdParticipante(idParticipante).forEach(
                participanteEquipo -> {
                    Equipos equipos = equiposRepository.findById(participanteEquipo.getIdEquipo())
                            .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

                    if(equipos.isActivo()) {
                        idEtapa.set(etapaEquipoRepository.findByIdEquipo(equipos.getIdEquipo()).getIdEtapa());
                    }
                }
        );

        return idEtapa.get();
    }
}

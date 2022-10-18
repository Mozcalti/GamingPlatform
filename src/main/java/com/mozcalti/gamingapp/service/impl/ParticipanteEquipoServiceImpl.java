package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.ParticipanteEquipo;
import com.mozcalti.gamingapp.repository.ParticipanteEquipoRepository;
import com.mozcalti.gamingapp.service.ParticipanteEquipoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ParticipanteEquipoServiceImpl extends GenericServiceImpl<ParticipanteEquipo, Integer> implements ParticipanteEquipoService {

    private ParticipanteEquipoRepository participanteEquipoRepository;

    @Override
    public CrudRepository<ParticipanteEquipo, Integer> getDao() {
        return participanteEquipoRepository;
    }
}

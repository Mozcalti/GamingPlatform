package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.repository.ParticipantesRepository;
import com.mozcalti.gamingapp.service.ParticipantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipantesServiceImpl extends GenericServiceImpl<Participantes, Integer> implements ParticipantesService {

    @Autowired
    private ParticipantesRepository participantesRepository;

    @Override
    public CrudRepository<Participantes, Integer> getDao() {
        return participantesRepository;
    }
}

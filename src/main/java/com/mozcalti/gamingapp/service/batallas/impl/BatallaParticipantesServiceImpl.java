package com.mozcalti.gamingapp.service.batallas.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.BatallaParticipantes;
import com.mozcalti.gamingapp.repository.BatallaParticipantesRepository;
import com.mozcalti.gamingapp.service.batallas.BatallaParticipantesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BatallaParticipantesServiceImpl extends GenericServiceImpl<BatallaParticipantes, Integer> implements BatallaParticipantesService {

    private BatallaParticipantesRepository batallaParticipantesRepository;

    @Override
    public CrudRepository<BatallaParticipantes, Integer> getDao() {
        return batallaParticipantesRepository;
    }
}

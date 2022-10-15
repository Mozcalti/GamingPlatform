package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.service.TorneosService;
import com.mozcalti.gamingapp.model.*;
import com.mozcalti.gamingapp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class TorneosServiceImpl extends GenericServiceImpl<Torneos, Integer> implements TorneosService {

    private TorneosRepository torneosRepository;

    @Override
    public CrudRepository<Torneos, Integer> getDao() {
        return torneosRepository;
    }

}

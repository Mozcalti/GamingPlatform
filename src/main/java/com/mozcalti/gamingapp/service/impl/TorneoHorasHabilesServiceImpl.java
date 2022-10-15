package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.TorneoHorasHabiles;
import com.mozcalti.gamingapp.repository.TorneoHorasHabilesRepository;
import com.mozcalti.gamingapp.service.TorneoHorasHabilesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class TorneoHorasHabilesServiceImpl extends GenericServiceImpl<TorneoHorasHabiles, Integer> implements TorneoHorasHabilesService {

    private TorneoHorasHabilesRepository torneoHorasHabilesRepository;
    @Override
    public CrudRepository<TorneoHorasHabiles, Integer> getDao() {
        return torneoHorasHabilesRepository;
    }
}

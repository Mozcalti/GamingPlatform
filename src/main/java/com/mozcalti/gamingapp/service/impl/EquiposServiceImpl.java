package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Equipos;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.service.EquiposService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EquiposServiceImpl extends GenericServiceImpl<Equipos, Integer> implements EquiposService {

    private EquiposRepository equiposRepository;

    @Override
    public CrudRepository<Equipos, Integer> getDao() {
        return equiposRepository;
    }
}

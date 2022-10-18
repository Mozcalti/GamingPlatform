package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.service.EtapasService;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.repository.EtapasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EtapasServiceImpl extends GenericServiceImpl<Etapas, Integer> implements EtapasService {

    private EtapasRepository etapasRepository;

    @Override
    public CrudRepository<Etapas, Integer> getDao() {
        return etapasRepository;
    }

}

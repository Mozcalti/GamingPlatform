package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.EtapaBatalla;
import com.mozcalti.gamingapp.repository.EtapaBatallaRepository;
import com.mozcalti.gamingapp.service.EtapaBatallaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EtapaBatallaServiceImpl extends GenericServiceImpl<EtapaBatalla, Integer> implements EtapaBatallaService {

    private EtapaBatallaRepository etapaBatallaRepository;

    @Override
    public CrudRepository<EtapaBatalla, Integer> getDao() {
        return etapaBatallaRepository;
    }
}

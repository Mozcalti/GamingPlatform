package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.EtapaEquipo;
import com.mozcalti.gamingapp.repository.EtapaEquipoRepository;
import com.mozcalti.gamingapp.service.EtapaEquipoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class EtapaEquipoServiceImpl extends GenericServiceImpl<EtapaEquipo, Integer> implements EtapaEquipoService {

    private EtapaEquipoRepository etapaEquipoRepository;
    
    @Override
    public CrudRepository<EtapaEquipo, Integer> getDao() {
        return etapaEquipoRepository;
    }
}

package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.EtapasEntity;
import com.mozcalti.gamingapp.model.ReglasEntity;
import com.mozcalti.gamingapp.repository.ReglasDao;
import com.mozcalti.gamingapp.service.EtapasService;
import com.mozcalti.gamingapp.service.ReglasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ReglasServiceImpl extends GenericServiceImpl<ReglasEntity, Integer> implements ReglasService {

    @Autowired
    private ReglasDao reglasDao;
    @Override
    public CrudRepository<ReglasEntity, Integer> getDao() {
        return reglasDao;
    }

}

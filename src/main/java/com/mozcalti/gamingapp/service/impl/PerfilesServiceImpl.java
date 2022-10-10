package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.PerfilesEntity;
import com.mozcalti.gamingapp.repository.PerfilesDao;
import com.mozcalti.gamingapp.service.PerfilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class PerfilesServiceImpl extends GenericServiceImpl<PerfilesEntity, Integer> implements PerfilesService {

    @Autowired
    private PerfilesDao perfilesDao;

    @Override
    public CrudRepository<PerfilesEntity, Integer> getDao() {
        return perfilesDao;
    }
}

package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Perfiles;
import com.mozcalti.gamingapp.repository.PerfilesRepository;
import com.mozcalti.gamingapp.service.PerfilesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PerfilesServiceImpl extends GenericServiceImpl<Perfiles, Integer> implements PerfilesService {

    private PerfilesRepository perfilesRepository;

    @Override
    public CrudRepository<Perfiles, Integer> getDao() {
        return perfilesRepository;
    }
}

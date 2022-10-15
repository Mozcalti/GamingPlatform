package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.repository.BatallasRepository;
import com.mozcalti.gamingapp.service.BatallasService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BatallasServiceImpl extends GenericServiceImpl<Batallas, Integer> implements BatallasService {

    private BatallasRepository batallasRepository;

    @Override
    public CrudRepository<Batallas, Integer> getDao() {
        return batallasRepository;
    }

}

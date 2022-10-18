package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.service.ReglasService;
import com.mozcalti.gamingapp.model.Reglas;
import com.mozcalti.gamingapp.repository.ReglasRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ReglasServiceImpl extends GenericServiceImpl<Reglas, Integer> implements ReglasService {

    private ReglasRepository reglasRepository;
    @Override
    public CrudRepository<Reglas, Integer> getDao() {
        return reglasRepository;
    }

}

package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.EtapaBatalla;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EtapaBatallaRepository extends CrudRepository<EtapaBatalla, Integer> {

    List<EtapaBatalla> findAllByIdEtapa(Integer idEtapa);

}

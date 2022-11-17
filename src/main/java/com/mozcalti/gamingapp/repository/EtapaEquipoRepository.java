package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.EtapaEquipo;
import org.springframework.data.repository.CrudRepository;

public interface EtapaEquipoRepository extends CrudRepository<EtapaEquipo, Integer> {

    EtapaEquipo findByIdEquipoAndIdEtapa(Integer idEquipo, Integer idEtapa);

    EtapaEquipo findByIdEquipo(Integer idEquipo);



}

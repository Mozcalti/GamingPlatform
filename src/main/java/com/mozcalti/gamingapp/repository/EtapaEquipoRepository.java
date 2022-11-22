package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.EtapaEquipo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EtapaEquipoRepository extends CrudRepository<EtapaEquipo, Integer> {

    EtapaEquipo findByIdEquipoAndIdEtapa(Integer idEquipo, Integer idEtapa);

    EtapaEquipo findByIdEquipo(Integer idEquipo);

    List<EtapaEquipo> findAllByIdEtapa(Integer idEtapa);

    void deleteByIdEquipo(Integer idEquipo);

}

package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Torneos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface TorneosRepository extends CrudRepository<Torneos, Integer> {

}

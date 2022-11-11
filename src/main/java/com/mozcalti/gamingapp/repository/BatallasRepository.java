package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Batallas;
import org.springframework.data.repository.CrudRepository;

public interface BatallasRepository extends CrudRepository<Batallas, Integer> {

    Batallas findByViewToken(String token);

}

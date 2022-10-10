package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.TorneosEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface TorneosDao extends CrudRepository<TorneosEntity, Integer> {

}

package com.mozcalti.gamingapp.repository;

import com.mozcalti.gamingapp.model.Resultados;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResultadosRepository extends PagingAndSortingRepository<Resultados, Integer>, JpaSpecificationExecutor<Resultados> {

}

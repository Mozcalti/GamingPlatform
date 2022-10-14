package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TablaDTO<T> {

    private List<T> lista;
    private PaginadoDTO paginadoDTO;
}

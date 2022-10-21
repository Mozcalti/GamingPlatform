package com.mozcalti.gamingapp.service.resultados;

import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.dto.TablaInstitucionDTO;

public interface DashboardService {

    void buscaSalidaBatallas();

    TablaDTO<ResultadosDTO> listaResultadosBatalla(String cadena, Integer indice);

}

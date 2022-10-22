package com.mozcalti.gamingapp.jobs;

import com.mozcalti.gamingapp.service.resultados.DashboardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CargaResultadosBatallas {

    private DashboardService dashboardsGlobalResultadosService;

    @Scheduled(cron = "${cron.resultados-batallas}")
    public void cargaResultadosBatallas() {
        dashboardsGlobalResultadosService.buscaSalidaBatallas();
    }

}

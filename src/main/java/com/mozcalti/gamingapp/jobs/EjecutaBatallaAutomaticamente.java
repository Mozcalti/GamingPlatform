package com.mozcalti.gamingapp.jobs;

import com.mozcalti.gamingapp.service.batallas.BatallasService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EjecutaBatallaAutomaticamente {

    private BatallasService batallasService;

    @Scheduled(cron = "${cron.ejecuta-batallas}")
    public void cargaResultadosBatallas() {
        batallasService.ejecutaBatalla();
    }

}

package com.mozcalti.gamingapp.service.correos;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.Batallas;

public interface SendMailBatalla {

    void mailInicioBatallas() throws UtilsException;

}

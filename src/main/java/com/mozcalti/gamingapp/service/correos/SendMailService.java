package com.mozcalti.gamingapp.service.correos;

import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;

import java.util.Map;

public interface SendMailService {

    void sendMail(String toAddress, String templateKey, Map<String, Object> templateParameters)
            throws SendMailException, UtilsException;
}

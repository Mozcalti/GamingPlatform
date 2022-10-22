package com.mozcalti.gamingapp.service.correos;

import com.mozcalti.gamingapp.exceptions.SendMailException;
import com.mozcalti.gamingapp.exceptions.UtilsException;

import java.util.Map;

public interface SendMailService {

    void sendMail(String toAddress, String subject, String templateMessage, Map<String, String> imagesMessage)
            throws SendMailException, UtilsException;

    public String readMailTemplate(String pathname, Map<String, Object> parameters)
            throws SendMailException, UtilsException;

}

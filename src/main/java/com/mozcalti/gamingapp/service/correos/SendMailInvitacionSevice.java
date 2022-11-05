package com.mozcalti.gamingapp.service.correos;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.model.Participantes;

public interface SendMailInvitacionSevice {
     void mailsInvitacion( Participantes participantes) throws UtilsException;
}

package com.mozcalti.gamingapp.validations;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.utils.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DashboardsGlobalResultadosValidation {

    public static void validaBatalla(Batallas batallas) throws ValidacionException {
         if(batallas == null) {
             throw new ValidacionException("No existe batalla");
         }
    }

    public static void validaExisteArchivoXML(String fileResultadoBatallas) throws ValidacionException {
        try {
            if(!FileUtils.isFileValid(fileResultadoBatallas)) {
                throw new ValidacionException("No existe archivo XML de termino de batalla");
            }
        } catch (UtilsException e) {
            throw new ValidacionException("Error al checar archivo XML Resultados", e);
        }
    }

}

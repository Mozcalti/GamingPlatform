package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static boolean isValidPattern(String value, String expReg) throws UtilsException {
        try {
            Pattern pattern = Pattern.compile(expReg);
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        } catch (Exception e) {
            throw new UtilsException("Error en el metodo: isValidPattern(String value, String expReg)"
                    + "\nMensaje de error: " + e.getMessage(), e);
        }
    }

}

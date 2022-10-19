package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.UtilsException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class StackTraceUtils {
    private StackTraceUtils() {
        throw new IllegalStateException("Utility StackTraceUtils");
    }

    public static String getCause(Throwable aThrowable) throws UtilsException {
        String causeError = "No se detecto la causa del error";
        try {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            if (aThrowable.getCause() != null) {
                Throwable cause = aThrowable.getCause();
                cause.printStackTrace(printWriter);
                causeError = result.toString();

                if (causeError == null) {
                    causeError = aThrowable.getMessage();
                }
            } else {
                causeError = aThrowable.getMessage();
            }
        } catch (Exception e) {
            throw new UtilsException("Error generado en el servicio: getCause(Throwable aThrowable)", e);
        }

        return causeError;
    }

    public static String getCustomStackTrace(Throwable aThrowable) throws UtilsException {
        try {
            final StringBuilder result = new StringBuilder("ERROR: ");
            result.append(aThrowable.toString());
            final String NEW_LINE = System.getProperty("line.separator");
            result.append(NEW_LINE);

            for (StackTraceElement element : aThrowable.getStackTrace()) {
                result.append(element);
                result.append(NEW_LINE);
            }
            return result.toString();
        } catch (Exception e) {
            throw new UtilsException("Error en el metodo: getCustomStackTrace(Throwable aThrowable)"
                    + "\nMensaje de error: " + e.getMessage(), e);
        }
    }

}

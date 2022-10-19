package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.UtilsException;

public final class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility FileUtils");
    }

    public static String getFileName(String filePathName) throws UtilsException {

        String nombreArchivo = "";
        int pos = 0;

        try {
            pos = filePathName.lastIndexOf('/');
            if (pos != -1) {
                nombreArchivo = filePathName.substring(pos + 1, filePathName.length());
            } else {
                pos = filePathName.lastIndexOf('\\');
                if (pos != -1) {
                    nombreArchivo = filePathName.substring(pos + 1, filePathName.length());
                } else {
                    nombreArchivo = filePathName;
                }
            }
        } catch (Exception e) {
            throw new UtilsException("Error en el metodo: getFileName():\n" + StackTraceUtils.getCause(e), e);
        }

        return nombreArchivo;
    }

    public static String getFileRuta(String filePathName) throws UtilsException {

        String ruta = "";
        int pos = 0;

        try {
            pos = filePathName.lastIndexOf('/');
            if (pos != -1) {
                ruta = filePathName.substring(0, pos);
            } else {
                pos = filePathName.lastIndexOf('\\');
                if (pos != -1) {
                    ruta = filePathName.substring(0, pos);
                } else {
                    ruta = "/";
                }
            }
        } catch (Exception e) {
            throw new UtilsException("Error en el metodo: getFileRuta():\n" + StackTraceUtils.getCause(e), e);
        }

        return ruta;
    }

}

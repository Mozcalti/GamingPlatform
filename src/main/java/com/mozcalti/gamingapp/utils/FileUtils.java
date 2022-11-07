package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.UtilsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    private static final String UTF8_BOM = "\uFEFF";
    private static final String DEFAULT_ENCODING = "UTF-8";

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

    public static boolean isFileValid(String fileName) throws UtilsException {
        boolean resultado = false;

        try {

            if (fileName != null && !fileName.isEmpty()) {
                File file = new File(fileName);

                if ((file.isFile() && file.exists()) || file.isDirectory()) {
                    resultado = true;
                }
            }

        } catch (Exception e) {
            throw new UtilsException("Error en el metodo: isFileValid():\n" + StackTraceUtils.getCause(e), e);
        }

        return resultado;
    }

    public static StringBuilder getRecordInfo(String nombreArchivo, String patternInicio, String patternFin) throws UtilsException {

        Scanner scanner = null;
        StringBuilder lineas = new StringBuilder();
        StringBuilder linea = new StringBuilder();

        try {
            int bndSeccionInicio = 0;
            if (FileUtils.isFileValid(nombreArchivo)) {
                    FileInputStream inputStream = new FileInputStream(nombreArchivo);
                    scanner = new Scanner(inputStream, StandardCharsets.UTF_8);

                while (scanner.hasNextLine()) {
                    linea.delete(0, linea.length());
                    linea.append(FileUtils.removeUTF8BOM(scanner.nextLine()));

                    if(bndSeccionInicio == 2) {
                        break;
                    }

                    if (StringUtils.isValidPattern(linea.toString(), patternInicio) && bndSeccionInicio == 0) {
                        bndSeccionInicio = 1;
                        lineas.append(linea).append("\n");
                    } else if(bndSeccionInicio == 1 && !StringUtils.isValidPattern(linea.toString(), patternFin)) {
                        lineas.append(linea).append("\n");
                    }

                    if (StringUtils.isValidPattern(linea.toString(), patternFin) && bndSeccionInicio == 1) {
                        bndSeccionInicio = 2;
                        lineas.append(linea).append("\n");
                    }
                }
            }

            return lineas;

        } catch (Exception e) {
            throw new UtilsException("Error en el servicio readFileToPattern():\n" + e, e);
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }

    }

    public static String removeUTF8BOM(String s) throws UtilsException {
        try {
            if (s.startsWith(UTF8_BOM)) {
                s = s.substring(1);
            }
            return s;
        } catch (Exception e) {
            throw new UtilsException("Error generado en el metodo: removeUTF8BOM(String s)"
                    + "\nMensaje de error: " + e.getMessage(), e);
        }
    }

    public static String encodeImageToString(String path) {
        try (FileInputStream file = new FileInputStream(path)) {
            return "data:image/png;base64," + Base64.encodeBase64String(file.readAllBytes());
        } catch (IOException exception) {
            throw new IllegalArgumentException(String.format("La imagen no es correcta %s", exception));
        }
    }

}

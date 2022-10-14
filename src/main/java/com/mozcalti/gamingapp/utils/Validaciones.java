package com.mozcalti.gamingapp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validaciones {

    public static String formatoFecha(){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public static String validaStringCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().length() >= Numeros.TRES.retornarNumero())
            return cell.getStringCellValue();
        else
            throw new IllegalArgumentException(String.format("El valor ubicado en la columna %s fila %s no es una cadena de texto", CellReference.convertNumToColString(cell.getColumnIndex()), cell.getRowIndex() + 1));
    }
    public static String validaEmailCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING && patternMatches(cell.getStringCellValue()) || cell.getStringCellValue().isBlank())
            return cell.getStringCellValue();
        else
            throw new IllegalArgumentException(String.format("El valor ubicado en la columna %s fila %s no es un correo valido", CellReference.convertNumToColString(cell.getColumnIndex()), cell.getRowIndex() + 1));
    }
    public static boolean patternMatches(String emailAddress) {
        return Pattern.matches("([a-z0-9].{1,64})@([a-z0-9]{4,255}.[a-z0-9]{2,4}){0,256}$",emailAddress);
    }
    public static XSSFWorkbook getWorkbook(String nombre, InputStream inputStream) throws IOException {
        if (inputStream == null || nombre == null)
            throw new IllegalArgumentException("Agrega un archivo para validar");
        else if (nombre.endsWith("xlsx") || nombre.endsWith("xls") || nombre.endsWith("csv"))
            return new XSSFWorkbook(inputStream);
        throw new IllegalArgumentException(String.format("El formato del Archivo '%s' no es un archivo de Excel o CSV", nombre));

    }
}

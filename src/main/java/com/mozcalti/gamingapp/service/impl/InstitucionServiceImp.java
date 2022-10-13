package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.service.InstitucionService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InstitucionServiceImp implements InstitucionService {
    private InstitucionRepository institucionRepository;
    @Override
    public List<Institucion> cargarArchivo(MultipartFile file) {
        try {
            List<Institucion> listadoInstituciones = new ArrayList<>();
            XSSFWorkbook workbook = getWorkbook(file.getOriginalFilename(),file.getInputStream());
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
            iterator.next();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Institucion institucion = new Institucion();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    if (nextCell.getColumnIndex() == 1)
                        institucion.setNombre(validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == 2)
                        institucion.setCorreo(validaEmailCellValue(nextCell));
                    institucion.setFechaCreacion(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                if (institucionRepository.findByNombre(institucion.getNombre()) != null)
                    throw new DuplicateKeyException(String.format("La Institucion '%s' ya esta registrada en el sistema", institucion.getNombre()));
                else
                    listadoInstituciones.add(institucion);

            }
            workbook.close();
            file.getInputStream().close();
            return listadoInstituciones;

        } catch (IOException ioException) {
            throw new IllegalArgumentException("Paso algo con el archivo, El equipo de desarrollo esta trabajando para solucionar el error");
        }
    }
    @Override
    public List<Institucion> guardarInstituciones(List<Institucion> instituciones) {
        for (Institucion institucion : instituciones) {
            if (institucionRepository.findByNombre(institucion.getNombre()) != null)
                throw new DuplicateKeyException(String.format("La Institucion '%s' ya esta registrada en el sistema", institucion.getNombre()));
        }
        return institucionRepository.saveAll(instituciones);
    }

    private String validaStringCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().length() >= 3)
            return cell.getStringCellValue();
        else
            throw new IllegalArgumentException(String.format("El valor ubicado en la columna %s fila %s no es una cadena de texto", CellReference.convertNumToColString(cell.getColumnIndex()), cell.getRowIndex() + 1));
    }
    private String validaEmailCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING && patternMatches(cell.getStringCellValue()) || cell.getStringCellValue().isBlank())
            return cell.getStringCellValue();
        else
            throw new IllegalArgumentException(String.format("El valor ubicado en la columna %s fila %s no es un correo Valido", CellReference.convertNumToColString(cell.getColumnIndex()), cell.getRowIndex() + 1));
    }
    private boolean patternMatches(String emailAddress) {
        return Pattern.compile("^(.+)@(.+)$")
                .matcher(emailAddress)
                .matches();
    }
    private XSSFWorkbook getWorkbook(String nombre, InputStream inputStream) throws IOException {
        if (inputStream == null || nombre == null)
            throw new IllegalArgumentException("Agrega un archivo para validar");
        else if (nombre.endsWith("xlsx") || nombre.endsWith("xls") || nombre.endsWith("csv"))
            return new XSSFWorkbook(inputStream);
        throw new IllegalArgumentException(String.format("El formato del Archivo '%s' no es un archivo de Excel o CSV", nombre));

    }
}

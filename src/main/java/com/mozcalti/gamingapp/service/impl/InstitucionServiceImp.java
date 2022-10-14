package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.service.InstitucionService;
import com.mozcalti.gamingapp.utils.Numeros;
import com.mozcalti.gamingapp.utils.Validaciones;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@AllArgsConstructor
public class InstitucionServiceImp implements InstitucionService {
    private InstitucionRepository institucionRepository;
    @Override
    public List<InstitucionDTO> cargarArchivo(MultipartFile file) {
        try {
            List<InstitucionDTO> listadoInstituciones = new ArrayList<>();
            XSSFWorkbook workbook = Validaciones.getWorkbook(file.getOriginalFilename(),file.getInputStream());
            Sheet firstSheet = workbook.getSheetAt(Numeros.CERO.getNumero());
            Iterator<Row> iterator = firstSheet.iterator();
            iterator.next();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Institucion institucion = new Institucion();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    if (nextCell.getColumnIndex() == Numeros.UNO.getNumero())
                        institucion.setNombre(Validaciones.validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.DOS.getNumero())
                        institucion.setCorreo(Validaciones.validaEmailCellValue(nextCell));
                }
                if (institucionRepository.findByNombre(institucion.getNombre()) != null)
                    throw new DuplicateKeyException(String.format("La institución '%s' ya esta registrada en el sistema", institucion.getNombre()));
                else
                    listadoInstituciones.add(new InstitucionDTO(institucion.getNombre(), institucion.getCorreo(), Validaciones.formatoFecha()));
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
        return institucionRepository.saveAll(instituciones);
    }


}

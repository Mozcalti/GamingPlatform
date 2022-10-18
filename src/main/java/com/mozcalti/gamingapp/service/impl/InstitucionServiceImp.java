package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.dto.InstitucionDTO;
import com.mozcalti.gamingapp.model.dto.PaginadoDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.dto.TablaInstitucionDTO;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.service.InstitucionService;
import com.mozcalti.gamingapp.utils.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InstitucionServiceImp implements InstitucionService, Utils, TablaInterface {
    private final InstitucionRepository institucionRepository;

    @Value("${resources.static.instituciones}")
    private String pathInstituciones;


    @Override
    public List<InstitucionDTO> cargarArchivo(MultipartFile file) {
        try {
            List<InstitucionDTO> listadoInstituciones = new ArrayList<>();
            XSSFWorkbook workbook = Validaciones.getWorkbook(file.getOriginalFilename(), file.getInputStream());

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
                    listadoInstituciones.add(new InstitucionDTO(institucion.getNombre(), institucion.getCorreo()));
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
            institucion.setFechaCreacion(FORMATTER.format(LOCAL_DATE_TIME));
            institucion.setLogo(encodeImageToString(pathInstituciones));
        }
        return (List<Institucion>) institucionRepository.saveAll(instituciones);
    }

    @Override
    public TablaDTO<TablaInstitucionDTO> listaInstituciones(String cadena, Integer indice) {
        Specification<Institucion> query = Specification.where(containsTextInAttributes(cadena, Arrays.asList("nombre", "fechaCreacion")));
        Page<Institucion> institucionPages = institucionRepository.findAll(query, PageRequest.of(indice, 50));
        PaginadoDTO paginadoDTO = new PaginadoDTO(institucionPages.getTotalPages(), institucionPages.getNumber());
        List<Institucion> institucionParte = institucionPages.toList();

        List<TablaInstitucionDTO> institucionesDTO = institucionParte.stream()
                .map(i -> new TablaInstitucionDTO(
                        i.getId(),
                        i.getNombre(),
                        i.getCorreo(),
                        i.getFechaCreacion(),
                        i.getLogo())
                ).toList();

        TablaDTO<TablaInstitucionDTO> tablaDTO = new TablaDTO<>();
        tablaDTO.setLista(institucionesDTO);
        tablaDTO.setPaginadoDTO(paginadoDTO);

        return tablaDTO;
    }

    @Override
    public TablaInstitucionDTO obtenerInstitucion(UUID id) {
        Optional<Institucion> institucion = institucionRepository.findById(id);
        if (institucion.isEmpty()) {
            throw new NoSuchElementException("La institución no se encuentra en el sistema");
        } else
            return new TablaInstitucionDTO(institucion.get().getId(), institucion.get().getNombre(), institucion.get().getCorreo(), institucion.get().getFechaCreacion(), institucion.get().getLogo());

    }

    public Specification<Institucion> containsTextInAttributes(String text, List<String> attributes) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(root.getModel().getDeclaredAttributes().stream()
                .filter(a -> attributes.contains(a.getName()))
                .map(c -> criteriaBuilder.like(root.get(c.getName()), "%" + text + "%"))
                .toArray(Predicate[]::new)
        ));
    }


    @Override
    public String encodeImageToString(String path) {
        try (FileInputStream file = new FileInputStream(path + "/institucionLogoDefaul.png")) {
            return Base64.encodeBase64String(file.readAllBytes());
        } catch (IOException exception) {
            throw new IllegalArgumentException(String.format("La imagen no es correcta %s", exception));
        }
    }
}

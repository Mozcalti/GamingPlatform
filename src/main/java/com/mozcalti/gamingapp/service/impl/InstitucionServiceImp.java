package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.model.Institucion;
import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.repository.ParticipantesRepository;
import com.mozcalti.gamingapp.service.InstitucionService;
import com.mozcalti.gamingapp.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstitucionServiceImp implements InstitucionService {
    private final InstitucionRepository institucionRepository;
    private final ParticipantesRepository participantesRepository;

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
                if (institucionRepository.findByNombre(institucion.getNombre()).isPresent())
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
            institucion.setFechaCreacion(DateUtils.now());
            institucion.setLogo(FileUtils.encodeImageToString(pathInstituciones + "/institucionLogoDefaul.png"));
        }
        return (List<Institucion>) institucionRepository.saveAll(instituciones);
    }

    @Override
    public TablaDTO<TablaInstitucionDTO> listaInstituciones(String cadena, String fecha, Integer indice) {
        Specification<Institucion> query = Specification.where(containsTextInAttributes(cadena, fecha, "nombre"));
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
    public DetalleInstitucionDTO obtenerInstitucion(Integer id) {
        Optional<Institucion> institucion = institucionRepository.findById(id);

        List<Participantes> participantes = participantesRepository.findAllByInstitucionIdOrderByNombreAsc(id);
        List<ParticipanteDTO> participanteDTOList = new ArrayList<>();
        for (Participantes participante : participantes) {
            participanteDTOList.add(new ParticipanteDTO(
                    participante.getNombre(),
                    participante.getApellidos(),
                    participante.getCorreo(),
                    participante.getAcademia(),
                    participante.getIes(),
                    participante.getCarrera(),
                    participante.getSemestre(),
                    participante.getInstitucion().getId()));
        }
        if (institucion.isEmpty()) {
            throw new NoSuchElementException("La institución no se encuentra en el sistema");
        } else
            return new DetalleInstitucionDTO(
                    institucion.get().getId(),
                    institucion.get().getNombre(),
                    institucion.get().getCorreo(),
                    institucion.get().getFechaCreacion(),
                    institucion.get().getLogo(),
                    participanteDTOList);

    }

    @Override
    public Institucion guardarInstitucion(InstitucionDTO institucionDTO) {
        Institucion institucion = new Institucion();
        if (institucionRepository.findByNombre(institucionDTO.getNombre()).isPresent())
            throw new DuplicateKeyException(String.format("La institución '%s' ya esta registrada en el sistema", institucionDTO.getNombre()));

        institucion.setNombre(Validaciones.validaStringValue(institucionDTO.getNombre()));
        institucion.setCorreo(Validaciones.validaEmailValue(institucionDTO.getCorreo()));
        institucion.setFechaCreacion(DateUtils.now());
        institucion.setLogo(FileUtils.encodeImageToString(pathInstituciones + "/institucionLogoDefaul.png"));

        return institucionRepository.save(institucion);
    }

    @Override
    public Iterable<Institucion> instituciones() {
        return institucionRepository.findAll();
    }

    private Specification<Institucion> containsTextInAttributes(String text, String fechaCreacion, String attributes) {
        try {
            if (fechaCreacion.isEmpty())
                return ((root, query, criteriaBuilder) -> criteriaBuilder.or(root.getModel().getDeclaredAttributes().stream()
                        .filter(a -> attributes.contains(a.getName()))
                        .map(c -> criteriaBuilder.like(root.get(c.getName()), "%" + text + "%"))
                        .toArray(Predicate[]::new)
                ));
            else {
                LocalDateTime localDateTime = LocalDateTime.from(DateTimeFormatter.ofPattern(Constantes.TIMESTAMP_PATTERN).parse(fechaCreacion));
                return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("fechaCreacion"), localDateTime.toLocalDate().atTime(LocalTime.MIN), localDateTime.toLocalDate().atTime(LocalTime.MAX)) );

            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("El formato de la fecha '%s' es incorrecto, el formato debe ser '%s'", fechaCreacion, Constantes.TIMESTAMP_PATTERN), e);
        }
    }
}
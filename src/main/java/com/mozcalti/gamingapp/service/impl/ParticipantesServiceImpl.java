package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.repository.ParticipantesRepository;
import com.mozcalti.gamingapp.service.ParticipantesService;
import com.mozcalti.gamingapp.utils.Numeros;
import com.mozcalti.gamingapp.utils.Utils;
import com.mozcalti.gamingapp.utils.Validaciones;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ParticipantesServiceImpl extends GenericServiceImpl<Participantes, Integer> implements ParticipantesService, Utils {

    @Autowired
    private ParticipantesRepository participantesRepository;

    @Autowired
    private InstitucionRepository institucionRepository;

    @Override
    public CrudRepository<Participantes, Integer> getDao() {
        return participantesRepository;
    }

    @Value("${resources.static.participantes}")
    private String pathParticipantes;

    @Override
    public List<ParticipanteDTO> cargarArchivo(MultipartFile file) {

        try {
            List<ParticipanteDTO> listadoParticipantes = new ArrayList<>();
            XSSFWorkbook workbook = Validaciones.getWorkbook(file.getOriginalFilename(), file.getInputStream());
            Sheet firstSheet = workbook.getSheetAt(Numeros.CERO.getNumero());
            Iterator<Row> iterator = firstSheet.iterator();
            iterator.next();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Participantes participantes = new Participantes();


                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    if (nextCell.getColumnIndex() == Numeros.UNO.getNumero())
                        participantes.setNombre(Validaciones.validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.DOS.getNumero())
                        participantes.setApellidos(Validaciones.validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.TRES.getNumero())
                        participantes.setCorreo(Validaciones.validaEmailCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.CUATRO.getNumero())
                        participantes.setAcademia(Validaciones.validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.CINCO.getNumero())
                        participantes.setIes(Validaciones.validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.SEIS.getNumero())
                        participantes.setCarrera(Validaciones.validaStringCellValue(nextCell));
                    if (nextCell.getColumnIndex() == Numeros.SIETE.getNumero())
                        participantes.setSemestre((int) nextCell.getNumericCellValue());
                    if (nextCell.getColumnIndex() == Numeros.OCHO.getNumero())
                        participantes.setIdInstitucion(institucionRepository.findByNombre(nextCell.getStringCellValue()).getId());
                }
                if (participantesRepository.findByCorreo(participantes.getCorreo()) != null)
                    throw new DuplicateKeyException("Ya esta registrado en el sistema un participante con correo " + "'" + participantes.getCorreo() + "'");
                else
                    listadoParticipantes.add(new ParticipanteDTO(
                            participantes.getNombre(),
                            participantes.getApellidos(),
                            participantes.getCorreo(),
                            participantes.getAcademia(),
                            participantes.getIes(),
                            participantes.getCarrera(),
                            participantes.getSemestre(),
                            participantes.getIdInstitucion()));
            }
            workbook.close();
            file.getInputStream().close();
            return listadoParticipantes;

        } catch (IOException ioException) {
            throw new IllegalArgumentException("Paso algo con el archivo, El equipo de desarrollo esta trabajando para solucionar el error");

        }
    }

    @Override
    public List<Participantes> guardarParticipantes(List<Participantes> participantes) {
        for (Participantes participante : participantes) {
            participante.setFechaCreacion(FORMATTER.format(Utils.LOCAL_DATE_TIME));
            participante.setFoto(encodeImageToString(pathParticipantes));
        }
        return (List<Participantes>) participantesRepository.saveAll(participantes);
    }

    @Override
    public TablaDTO<TablaParticipantesDTO> listaParticipantes(String cadena, Integer indice) {
        Specification<Participantes> query = Specification.where(containsTextInAttributes(cadena, Arrays.asList("nombre", "correo","institucion")));
        Page<Participantes> participantesPages = participantesRepository.findAll(query, PageRequest.of(indice, 50));
        PaginadoDTO paginadoDTO = new PaginadoDTO(participantesPages.getTotalPages(), participantesPages.getNumber());
        List<Participantes> participantesParte = participantesPages.toList();

        List<TablaParticipantesDTO> tablaParticipantesDTO = participantesParte.stream()
                .map(p -> new TablaParticipantesDTO(
                        p.getIdParticipante(),
                        p.getNombre(),
                        p.getApellidos(),
                        p.getCorreo(),
                        p.getAcademia(),
                        p.getIes(),
                        p.getCarrera(),
                        p.getSemestre(),
                        p.getFoto(),
                        p.getFechaCreacion(),
                        institucionRepository.findById(p.getIdInstitucion()).get().getNombre()
                )).toList();

        TablaDTO<TablaParticipantesDTO> tablaDTO = new TablaDTO<>();
        tablaDTO.setLista(tablaParticipantesDTO);
        tablaDTO.setPaginadoDTO(paginadoDTO);
        return tablaDTO;
    }

    @Override
    public TablaParticipantesDTO obtenerParticipante(Integer id) {
        Optional<Participantes> participantes = participantesRepository.findById(id);
        if (participantes.isEmpty()){
            throw new NoSuchElementException("No se encontro el participante en el sistema");
        }else
            return new TablaParticipantesDTO(
                    participantes.get().getIdParticipante(),
                    participantes.get().getNombre(),
                    participantes.get().getApellidos(),
                    participantes.get().getCorreo(),
                    participantes.get().getAcademia(),
                    participantes.get().getIes(),
                    participantes.get().getCarrera(),
                    participantes.get().getSemestre(),
                    participantes.get().getFoto(),
                    participantes.get().getFechaCreacion(),
                    institucionRepository.findById(participantes.get().getIdInstitucion()).get().getNombre());
    }

    private Specification<Participantes> containsTextInAttributes(String text, List<String> attributes) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(root.getModel().getDeclaredAttributes().stream()
                .filter(a -> attributes.contains(a.getName()))
                .map(c -> criteriaBuilder.like(root.get(c.getName()), "%" + text + "%"))
                .toArray(Predicate[]::new)
        ));
    }

    @Override
    public String encodeImageToString(String path) {
        try (FileInputStream file = new FileInputStream(path + "/participanteFotoDefaul.png")) {
            return Base64.encodeBase64String(file.readAllBytes());
        } catch (IOException exception) {
            throw new IllegalArgumentException(String.format("La imagen no es correcta %s", exception));
        }
    }
}

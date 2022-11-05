package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.repository.InstitucionRepository;
import com.mozcalti.gamingapp.repository.ParticipantesRepository;
import com.mozcalti.gamingapp.service.ParticipantesService;
import com.mozcalti.gamingapp.service.correos.SendMailInvitacionSevice;
import com.mozcalti.gamingapp.service.correos.impl.SendMailInvitacionServiceImpl;
import com.mozcalti.gamingapp.service.usuarios.UsuarioService;
import com.mozcalti.gamingapp.utils.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class ParticipantesServiceImpl extends GenericServiceImpl<Participantes, Integer> implements ParticipantesService {

    @Autowired
    private ParticipantesRepository participantesRepository;

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SendMailInvitacionSevice sendMailInvitacionSevice;

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
                        participantes.setInstitucion(institucionRepository.findByNombre(nextCell.getStringCellValue()).orElse(null));
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
                            participantes.getInstitucion().getId()));
            }
            workbook.close();
            file.getInputStream().close();
            return listadoParticipantes;

        } catch (IOException ioException) {
            throw new IllegalArgumentException("Paso algo con el archivo, El equipo de desarrollo esta trabajando para solucionar el error");

        }
    }

    @Override
    public List<Participantes> guardarParticipantes(List<ParticipanteDTO> participanteDTO) {
        List<Participantes> listadoParticipantes = new ArrayList<>();
        for (ParticipanteDTO dto : participanteDTO) {
            Participantes participante = new Participantes();
            participante.setNombre(dto.getNombre());
            participante.setApellidos(dto.getApellidos());
            participante.setCorreo(dto.getCorreo());
            participante.setAcademia(dto.getAcademia());
            participante.setIes(dto.getIes());
            participante.setCarrera(dto.getCarrera());
            participante.setSemestre(dto.getSemestre());
            participante.setFoto(FileUtils.encodeImageToString(pathParticipantes + "/participanteFotoDefaul.png"));
            participante.setFechaCreacion(DateUtils.now());
            participante.setInstitucion(institucionRepository.findById(dto.getIdInstitucion()).orElse(null));

            usuarioService.save(new UsuarioDTO(participante));
            sendMailInvitacionSevice.mailsInvitacion(new Participantes());
            listadoParticipantes.add(participante);
        }
        return (List<Participantes>) participantesRepository.saveAll(listadoParticipantes);
    }

    @Override
    public List<TablaParticipantesDTO> listaParticipantes(String cadena, String fechaCreacion) {
        Specification<Participantes> query = Specification.where(containsTextInAttributes(cadena, fechaCreacion ,Arrays.asList("nombre", "correo")))
                .or(containsTextInInstitucion(cadena));
        List<Participantes> participantesPages = participantesRepository.findAll(query);

        return participantesPages.stream()
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
                        p.getInstitucion().getId()
                )).toList();
    }

    @Override
    public TablaParticipantesDTO obtenerParticipante(Integer id) {
        Optional<Participantes> participantes = participantesRepository.findById(id);
        if (participantes.isEmpty()) {
            throw new NoSuchElementException("No se encontro el participante en el sistema");
        } else
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
                    participantes.get().getInstitucion().getId());
    }

    @Override
    public Participantes guardarParticipante(ParticipanteDTO participanteDTO) {
        Participantes participante = new Participantes();
        if (participantesRepository.findByCorreo(participanteDTO.getCorreo()) != null)
            throw new DuplicateKeyException(String.format("El participante '%s' ya esta registrado en el sistema", participanteDTO.getCorreo()));
        participante.setNombre(participanteDTO.getNombre());
        participante.setApellidos(participanteDTO.getApellidos());
        participante.setCorreo(participanteDTO.getCorreo());
        participante.setAcademia(participanteDTO.getAcademia());
        participante.setIes(participanteDTO.getIes());
        participante.setCarrera(participanteDTO.getCarrera());
        participante.setSemestre(participanteDTO.getSemestre());
        participante.setFoto(FileUtils.encodeImageToString(pathParticipantes + "/participanteFotoDefaul.png"));
        participante.setFechaCreacion(DateUtils.now());
        participante.setInstitucion(institucionRepository.findById(participanteDTO.getIdInstitucion()).orElse(null));

        usuarioService.save(new UsuarioDTO(participante));

        return participantesRepository.save(participante);
    }

    @Override
    public Participantes actualizarParticipante(TablaParticipantesDTO participanteDTO) {
        Participantes participanteDB = participantesRepository.findById(participanteDTO.getIdParticipante()).orElse(null);
        if (participanteDB != null) {
            participanteDB.setNombre(participanteDTO.getNombre());
            participanteDB.setApellidos(participanteDTO.getApellidos());
            participanteDB.setCorreo(participanteDTO.getCorreo());
            participanteDB.setAcademia(participanteDTO.getAcademia());
            participanteDB.setIes(participanteDTO.getIes());
            participanteDB.setCarrera(participanteDTO.getCarrera());
            participanteDB.setSemestre(participanteDTO.getSemestre());
            participanteDB.setFoto(participanteDTO.getFoto());
            participanteDB.setFechaCreacion(participanteDTO.getFechaCreacion());
            participanteDB.setInstitucion(institucionRepository.findById(participanteDTO.getIdInstitucion()).orElse(null));
            return participantesRepository.save(participanteDB);
        } else
            throw new NoSuchElementException(String.format("El participante con el id '%s' no se encuentra en el sistema", participanteDTO.getIdParticipante()));


    }

    private Specification<Participantes> containsTextInAttributes(String text, String fechaCreacion,List<String> attributes) {

        try{
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

        }catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("El formato de la fecha '%s' es incorrecto, el formato debe ser '%s'", fechaCreacion, Constantes.TIMESTAMP_PATTERN), e);
        }


    }
    private Specification<Participantes> containsTextInInstitucion(String nombreInstitucion) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("institucion").get("nombre"), nombreInstitucion));
    }

}

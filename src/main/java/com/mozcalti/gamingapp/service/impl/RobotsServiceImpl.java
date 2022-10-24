package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.RobotValidationException;
import com.mozcalti.gamingapp.model.Equipos;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.repository.RobotsRepository;
import com.mozcalti.gamingapp.robocode.BattleRunner;
import com.mozcalti.gamingapp.robocode.Robocode;
import com.mozcalti.gamingapp.service.RobotsService;
import com.mozcalti.gamingapp.utils.RobocodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class RobotsServiceImpl extends GenericServiceImpl<Robots, Integer> implements RobotsService {

    @Autowired
    private RobotsRepository robotsRepository;

    @Override
    public CrudRepository<Robots, Integer> getDao() {
        return null;
    }

    @Value("${robocode.executable}")
    private String pathRobocode;
    @Value("${robocode.robots}")
    private String pathRobots;
    private static final String DIRFLAG = ".*[/\\n\\r\\t\\0\\f`?*\\\\<>|\\\":].*\"";
    private static final String REPLAYTYPE = "xml";
    private static final String ROBOTEXTENSION = ".jar";
    private static final String TESTFILEID = "PRUEBA";
    private static final boolean TESTISRECORDED = true;
    private static final int TESTSIZE = 800;
    private static final int TESTROUNDS = 1;
    private static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|'};
    private static final Character[] INVALID_UNIX_SPECIFIC_CHARS = {'\000'};

    @Override
    public RobotsDTO cargarRobot(int idEquipo, String tipo, MultipartFile file) throws IOException {
        if (file != null) {
            if(!file.isEmpty()){
                byte[] bytes;
                bytes = file.getBytes();
                if(safetyCheckForFileName(file)){
                    File copiedFile = new File(pathRobots + File.separator + file.getOriginalFilename());
                    return validateRobotJar(copiedFile, file.getOriginalFilename(), tipo, idEquipo, bytes);
                }
            } else {
                throw new RobotValidationException("El archivo que intentas cargar esta vacío.");
            }
        }
        return null;
    }

    @Override
    public Robots guardarRobot(Robots robot) {
        return robotsRepository.save(robot);
    }

    @Override
    public void eliminarRobot(int idRobot) throws NoSuchFileException {
        Optional<Robots> robot = robotsRepository.findById(idRobot);
        if(robot.isPresent()){
            borrarRobot(robot.get().getNombre());
            robotsRepository.deleteByIdRobot(idRobot);
        }
    }

    @Override
    @Transactional
    public int seleccionarRobot(String nombreRobot, int idRobot){
        return robotsRepository.updateActivo(1, idRobot);
    }

    public List<Robots> obtenerRobots(int idEquipo){
        return robotsRepository.findAllByIdEquipo(idEquipo);
    }

    public RobotsDTO validateRobotJar(File serverFile, String fileName, String tipo, int idEquipo, byte[] bytes) throws IOException {
        if(fileName != null){
            if(fileName.endsWith(ROBOTEXTENSION)) {
                if (robotsRepository.findByNombre(fileName) != null) {
                    throw new DuplicateKeyException("Ya existe un robot con el nombre: " + "'" + fileName + "'");
                } else {
                    return validateRobot(bytes, serverFile, tipo, idEquipo, fileName);
                }
            }else{
                throw new RobotValidationException("El archivo que intentas cargar no es un robot empaquetado en formato JAR.");
            }
        }else{
            throw new NullPointerException("No se pudo obtener el nombre del archivo jar a cargar.");
        }
    }

    public RobotsDTO validateRobot(byte[] bytes, File serverFile, String tipo, int idEquipo, String fileName) throws IOException {

        String src = serverFile.getPath().replace('\\', '/');
        try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
        } catch (IOException e) {
            borrarRobot(fileName);
            throw new FileNotFoundException("Hubo un error al cargar el robot: " + e);
        }
        //el robot contiene solo .properties, el equipo puede tener .properties pero siempre tiene .team
        String extension = ".team";
        String testRobot;
        //debemos validar que el zip no vaya a explotarnos
        if(tipo.equals("robot")){
            //se piensa subir un robot, busca el .team, no lo encuentra, solo hay un .properties, entonces si es un robot
            if(!RobocodeUtils.isRobotType(src, extension)) {
                extension = ".class";
                testRobot = validateName(src, extension);            //obtenemos el nombre que necesita robocode para buscarlo y cargarlo en la batalla
            }else{
                log.error("Estas intentando subir a un team, no un robot" + src);
                borrarRobot(fileName);
                throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
            }
        }else{
            //se piensa subir un equipo, busca el .team, lo encuentra, entonces si es un equipo
            if(RobocodeUtils.isRobotType(src, extension)) {
                testRobot = validateName(src, extension);
            }else{
                borrarRobot(fileName);
                throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
            }
        }
        testRobot += "," + testRobot;
        BattleRunner br = new BattleRunner(new Robocode(), TESTFILEID, TESTISRECORDED,
                TESTSIZE, TESTSIZE, testRobot, TESTROUNDS);
        br.prepareRobocode(pathRobocode, REPLAYTYPE);
        br.runBattle(src);
        Robots robot = new Robots();
        robot.setNombre(fileName.replace(ROBOTEXTENSION, ""));
        robot.setActivo(0);
        robot.setIdEquipo(idEquipo);
        return new RobotsDTO(robot.getNombre(), robot.getActivo(), robot.getIdEquipo(), new Equipos());
    }

    public String validateName(String src, String extension) throws IOException {
        try{
            return RobocodeUtils.getRobotClassName(src, extension);
        }catch (RuntimeException e){
            Files.delete(Paths.get(src));
            throw new IndexOutOfBoundsException("El robot no contiene un className apropiado.");
        }
    }


    public boolean safetyCheckForFileName(MultipartFile file) throws IOException {
        if(file.getOriginalFilename() != null){
            String fileName = file.getOriginalFilename();
            File copiedFile = new File(fileName);
            File directory = new File(DIRFLAG);
            if(validateStringFilenameUsingContains(fileName)){
                try{
                    if(FileUtils.directoryContains(directory, copiedFile)){
                        FileUtils.forceDelete(copiedFile);
                        return false;
                    }
                }catch (IOException e){
                    throw new IOException("Archivo invalido");
                }
                return true;
            }
        }
        return false;
    }


    public static Character[] getInvalidCharsByOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return INVALID_WINDOWS_SPECIFIC_CHARS;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return INVALID_UNIX_SPECIFIC_CHARS;
        } else {
            return new Character[]{};
        }
    }

    public static boolean validateStringFilenameUsingContains(String filename) {
        if (filename == null || filename.isEmpty() || filename.length() > 255) {
            return false;
        }
        return Arrays.stream(getInvalidCharsByOS())
                .noneMatch(ch -> filename.contains(ch.toString()));
    }

    public void borrarRobot(String nombreRobot) throws NoSuchFileException {
        try {
            Files.delete(Paths.get(pathRobots + nombreRobot + ROBOTEXTENSION));
        } catch (IOException e) {
            throw new NoSuchFileException("El robot a borrar no existe");
        }
    }


}


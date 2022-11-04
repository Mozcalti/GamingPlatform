package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.exceptions.RobotValidationException;
import com.mozcalti.gamingapp.model.Equipos;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.repository.EquiposRepository;
import com.mozcalti.gamingapp.repository.RobotsRepository;
import com.mozcalti.gamingapp.robocode.BattleRunner;
import com.mozcalti.gamingapp.robocode.Robocode;
import com.mozcalti.gamingapp.service.RobotsService;
import com.mozcalti.gamingapp.utils.Numeros;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class RobotsServiceImpl extends GenericServiceImpl<Robots, Integer> implements RobotsService {

    @Autowired
    private RobotsRepository robotsRepository;

    @Autowired
    private EquiposRepository equiposRepository;
    @Override
    public CrudRepository<Robots, Integer> getDao() {
        return null;
    }

    @Value("${robocode.executable}")
    private String pathRobocode;
    @Value("${robocode.robots}")
    private String pathRobots;
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
                if(safetyCheckForFileName(file)){
                    byte[] bytes = file.getBytes();
                    return validateRobotJar(file.getOriginalFilename(), tipo, idEquipo, bytes);
                }
            } else {
                throw new RobotValidationException("El archivo que intentas cargar esta vacío.");
            }
        }
        return null;
    }

    @Override
    public Robots guardarRobot(Robots robot) {
        Optional<Equipos> equipo = equiposRepository.findById(robot.getIdRobot());
        if(equipo.isPresent()){
            robot.setEquiposByIdEquipo(equipo.get());
        }
        return robotsRepository.save(robot);
    }

    @Override
    public void eliminarRobot(int idRobot) throws IOException {
        Optional<Robots> robot = robotsRepository.findById(idRobot);
        if(robot.isPresent()){
            Path jarFile = Paths.get(pathRobots + File.separator + robot.get().getNombre());
            String finalPath = pathRobots + File.separator + UUID.randomUUID();
            Files.move(jarFile, jarFile.resolveSibling(finalPath));
            borrarRobot(String.valueOf(Paths.get(finalPath).getFileName()));
            robotsRepository.deleteById(idRobot);
        }
    }

    @Override
    @Transactional
    public void seleccionarRobot(String nombre, int idEquipo){
        deseleccionarRobots(idEquipo);
        Robots robot = robotsRepository.findByNombre(nombre);
        robot.setActivo(Numeros.UNO.getNumero());
    }

    public void deseleccionarRobots(int idEquipo){
        List<Robots> listaRobots = robotsRepository.findAllByIdEquipo(idEquipo);
        for (Robots robot: listaRobots) {
            robot.setActivo(Numeros.CERO.getNumero());
            robotsRepository.save(robot);
        }
    }
    @Override
    public List<RobotsDTO> obtenerRobots(Integer idEquipo){
        List<Robots> listaRobots = robotsRepository.findAllByIdEquipo(idEquipo);
        List<RobotsDTO> listaRobotsDTO = new ArrayList<>();
        for (Robots robot: listaRobots) {
            listaRobotsDTO.add(new RobotsDTO(robot.getIdRobot(), robot.getNombre(), robot.getActivo(), robot.getIdEquipo(), robot.getClassName(), robot.getTipo()));
        }
        return listaRobotsDTO;
    }

    public RobotsDTO validateRobotJar(String originalFileName, String tipo, int idEquipo, byte[] bytes) throws IOException {
        if(originalFileName != null){
            if(originalFileName.endsWith(ROBOTEXTENSION)) {
                log.error(originalFileName);
                if (robotsRepository.findByNombre(originalFileName) != null) {
                    throw new DuplicateKeyException("Ya existe un robot con el nombre: " + "'" + originalFileName + "'");
                } else {
                    if(Files.exists(Paths.get(pathRobots + File.separator + originalFileName))){

                        Path jarFile = Paths.get(pathRobots + File.separator + originalFileName);
                        File newFile = jarFile.toFile();
                        if(FileUtils.directoryContains(new File(pathRobots), newFile)){
                            String finalPath = pathRobots + File.separator + UUID.randomUUID();
                            Files.move(jarFile, jarFile.resolveSibling(finalPath));
                            borrarRobot(String.valueOf(Paths.get(finalPath).getFileName()));
                        }
                    }
                    Path path = Paths.get(pathRobots);
                    Path serverFile = Files.createTempFile(path, "robot", ".jar");
                    String serverFileName = serverFile.toFile().getName();
                    return validateRobot(originalFileName, bytes, serverFile, tipo, idEquipo, serverFileName);
                }
            }else{
                throw new RobotValidationException("El archivo que intentas cargar no es un robot empaquetado en formato JAR.");
            }
        }else{
            throw new NullPointerException("No se pudo obtener el nombre del archivo jar a cargar.");
        }
    }

    public RobotsDTO validateRobot(String originalFileName, byte[] bytes, Path serverFile, String tipo, int idEquipo, String tempFileName) throws IOException {

        String src = serverFile.toAbsolutePath().toString();
        try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile.toFile()))) {
            stream.write(bytes);
        } catch (IOException e) {
            borrarRobot(tempFileName);
            throw new FileNotFoundException("Hubo un error al cargar el robot: " + e);
        }
        //el robot contiene solo el archivo properties, el equipo puede tener el archivo properties, pero siempre tiene .team
        String extension = ".team";
        String testRobot;
        //debemos validar que el zip no vaya a explotarnos
        if(tipo.equals("robot")){
            //se piensa subir un robot, busca el archivo team, no lo encuentra, solo hay un .properties, entonces si es un robot
            if(!RobocodeUtils.isRobotType(src, extension)) {
                extension = ".class";
                testRobot = validateName(src, extension, tempFileName);            //obtenemos el nombre que necesita robocode para buscarlo y cargarlo en la batalla
            }else{
                borrarRobot(tempFileName);
                throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
            }
        }else{
            //se piensa subir un equipo, busca el .team, lo encuentra, entonces si es un equipo
            if(RobocodeUtils.isRobotType(src, extension)) {
                testRobot = validateName(src, extension, tempFileName);
            }else{
                borrarRobot(tempFileName);
                throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
            }
        }
        String className = testRobot;
        //aqui validar que el className no exista en la base de datos....
        if(robotsRepository.findByClassName(className) != null) {
            borrarRobot(tempFileName);
            throw new DuplicateKeyException("Ya existe un robot con el class name: " + "'" + className + "'");
        }else{
            testRobot += "," + testRobot;
            BattleRunner br = new BattleRunner(new Robocode(), TESTFILEID, TESTISRECORDED,
                    TESTSIZE, TESTSIZE, testRobot, TESTROUNDS);
            br.runRobotValidationBattle(serverFile, pathRobots, originalFileName, pathRobocode, REPLAYTYPE);
            Robots robot = new Robots();
            robot.setNombre(originalFileName);
            robot.setActivo(Numeros.CERO.getNumero());
            robot.setIdEquipo(idEquipo);
            robot.setClassName(className);
            robot.setTipo(tipo);
            return new RobotsDTO(robot.getIdRobot(), robot.getNombre(), robot.getActivo(), robot.getIdEquipo(), robot.getClassName(), robot.getTipo());
        }
    }

    public String validateName(String src, String extension, String tempFileName) throws NoSuchFileException {
        try{
            return RobocodeUtils.getRobotClassName(src, extension);
        }catch (IndexOutOfBoundsException e){
            borrarRobot(tempFileName);
            throw new RobotValidationException("El archivo del robot esta corrupto. Verificar al momento de empaquetarlo.");
        }catch (IOException e){
            throw new RobotValidationException("El archivo del robot que se intenta validar no existe.");
        }
    }

    public boolean safetyCheckForFileName(MultipartFile file){
        if(file.getOriginalFilename() != null){
            String fileName = file.getOriginalFilename();
            return(validateStringFilenameUsingContains(fileName));
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
            Files.delete(Paths.get(pathRobots + File.separator + nombreRobot));
        } catch (IOException e) {
            throw new NoSuchFileException("El robot a borrar no existe");
        }
    }
}
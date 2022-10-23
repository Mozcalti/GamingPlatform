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
import java.util.List;

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
    private static final String REPLAYTYPE = "xml";
    private static final String ROBOTEXTENSION = ".jar";
    private static final String TESTFILEID = "";
    private static final boolean TESTISRECORDED = false;
    private static final int TESTSIZE = 800;
    private static final int TESTROUNDS = 1;

    @Override
    public RobotsDTO cargarRobot(int idEquipo, String tipo, MultipartFile file) throws IOException {
        if (file != null) {
            if(!file.isEmpty()){
                byte[] bytes;
                bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                File copiedFile = copyFile(file);
                return validateRobotJar(copiedFile, fileName, tipo, idEquipo, bytes);
            }
        } else {
            throw new RobotValidationException("El archivo que intentas cargar esta vacío.");
        }
        return null;
    }

    @Override
    public Robots guardarRobot(Robots robot) {
        return robotsRepository.save(robot);
    }

    @Override
    public void eliminarRobot(String nombreRobot, int idRobot) throws NoSuchFileException {
        borrarRobot(nombreRobot);
        robotsRepository.deleteByIdRobot(idRobot);
    }

    public void borrarRobot(String nombreRobot) throws NoSuchFileException {
        try {
            Files.delete(Paths.get(pathRobots + nombreRobot + ROBOTEXTENSION));
        } catch (IOException e) {
            throw new NoSuchFileException("El robot a borrar no existe");
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

    public File copyFile(MultipartFile file){
        File dir = new File(pathRobots);
        return new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
    }

    public RobotsDTO validateRobotJar(File serverFile, String fileName, String tipo, int idEquipo, byte[] bytes) throws IOException {
        if(fileName != null){
            if(fileName.substring(fileName.length()-ROBOTEXTENSION.length()).equals(ROBOTEXTENSION)) {
                if (robotsRepository.findByNombre(fileName.replace(ROBOTEXTENSION, "")) != null) {
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
        try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
            //sonarlint sugiere remover esta llamada, pero si no se hace no se puede ejecutar la batalla con ese robot apenas creado
            stream.close();
            //el robot contiene solo .properties, el equipo puede tener .properties pero siempre tiene .team
            String extension = ".team";
            String testRobot;
            if(tipo.equals("robot")){
                //se piensa subir un robot, busca el .team, no lo encuentra, solo hay un .properties, entonces si es un robot
                if(!RobocodeUtils.isRobotType(serverFile.getPath().replace('\\', '/'), extension)) {
                    extension = ".class";
                    testRobot = validateName(serverFile, extension, fileName);            //obtenemos el nombre que necesita robocode para buscarlo y cargarlo en la batalla
                }else{
                    log.error("Estas intentando subir a un team, no un robot" + pathRobots + "/" + fileName);
                    Files.delete(Paths.get(pathRobots + "/" + fileName));
                    throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
                }
            }else{
                //se piensa subir un equipo, busca el .team, lo encuentra, entonces si es un equipo
                if(RobocodeUtils.isRobotType(serverFile.getPath().replace('\\', '/'), extension)) {
                    testRobot = validateName(serverFile, extension, fileName);
                }else{
                    Files.delete(Paths.get(pathRobots + "/" + fileName));
                    throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
                }
            }
                testRobot += "," + testRobot;
                BattleRunner br = new BattleRunner(new Robocode(), TESTFILEID, TESTISRECORDED,
                        TESTSIZE, TESTSIZE, testRobot, TESTROUNDS);
                br.prepareRobocode(pathRobocode, REPLAYTYPE);
                br.runBattle(pathRobots, fileName);
                Robots robot = new Robots();
                robot.setNombre(fileName.replace(ROBOTEXTENSION, ""));
                robot.setActivo(0);
                robot.setIdEquipo(idEquipo);
            return new RobotsDTO(robot.getNombre(), robot.getActivo(), robot.getIdEquipo(), new Equipos());
        } catch (IOException e) {
            Files.delete(Paths.get(pathRobots + "/" + fileName));
            throw new FileNotFoundException("Hubo un error al cargar el robot: " + e);
        }
    }

    public String validateName(File serverFile, String extension, String fileName) throws IOException {
        try{
            return RobocodeUtils.getRobotClassName(serverFile.getPath().replace('\\', '/'), extension);
        }catch (RuntimeException e){
            Files.delete(Paths.get(pathRobots + "/" + fileName));
            throw new IndexOutOfBoundsException("El robot no contiene un className apropiado.");
        }
    }
}


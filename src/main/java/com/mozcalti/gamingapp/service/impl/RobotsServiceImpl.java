package com.mozcalti.gamingapp.service.impl;

import com.mozcalti.gamingapp.commons.GenericServiceImpl;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.*;
import com.mozcalti.gamingapp.repository.RobotsRepository;
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

import com.mozcalti.gamingapp.exceptions.EmptyRobotException;

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


    @Value("${path.robocode}")
    private String pathRobocode;
    @Override
    public RobotsDTO cargarRobot(int idParticipante, String tipo, MultipartFile file) throws IOException {
        //archivo vacio?
        if (!file.isEmpty()) {
            //se intenta copiar el archivo
            byte[] bytes = new byte[0];
            //es posible copiar los datos del archivo?
            try {
                bytes = file.getBytes();
                String rootPath = pathRobocode;
                File dir = new File(rootPath + File.separator + "robots");
                if (!dir.exists())
                    dir.mkdirs();
                //se crea el archivo en robos
                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                //duplicado (se considera para llave el nombre)
                if (robotsRepository.findByNombre(file.getOriginalFilename().replace(".jar", "")) != null)
                    throw new DuplicateKeyException("Ya existe un robot con el nombre: " + "'" + file.getOriginalFilename() + "'");
                else
                    return RobocodeUtils.validateRobot(bytes, serverFile, tipo, idParticipante, file.getOriginalFilename(), pathRobocode);
            } catch (IOException e) {
                throw new IOException("El robot esta corrupto. Verifica tu archivo empaquetado." + e);
            }
        } else {
            throw new EmptyRobotException("El robot que intentas cargar esta vacío.");
        }
    }

    @Override
    public Robots guardarRobot(Robots robot) {
        return robotsRepository.save(robot);
    }

    @Override
    public void eliminarRobot(String nombreRobot, int idRobot) throws NoSuchFileException {

        try {
            Files.delete(Paths.get(pathRobocode + nombreRobot + ".jar"));
            log.error(pathRobocode + nombreRobot + ".jar");
            robotsRepository.deleteById(idRobot);
        } catch (IOException e) {
            throw new NoSuchFileException("El robot a borrar no existe");
        }
    }

    @Override
    @Transactional
    public int seleccionarRobot(String nombreRobot, int idRobot){
        return robotsRepository.updateActivo(1, idRobot);
    }


    public List<Robots> obtenerRobots(int idParticipante){
        return robotsRepository.findAllByIdParticipante(idParticipante);
    }

}

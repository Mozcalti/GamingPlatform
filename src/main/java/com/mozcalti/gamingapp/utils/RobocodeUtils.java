package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.RobotValidationException;
import com.mozcalti.gamingapp.model.Robots;
import com.mozcalti.gamingapp.model.dto.RobotsDTO;
import com.mozcalti.gamingapp.robocode.BattleRunner;
import com.mozcalti.gamingapp.robocode.Robocode;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RobocodeUtils {

    public static String getRobotClassName(String file, String type){

        //aqui tiene que checar, si es individual, buscar el properties
        //si es team, tiene que buscar el team
        ArrayList<String> classNames = new ArrayList<>();
        try(ZipInputStream zip = new ZipInputStream(new FileInputStream(file)))
        {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(type)) {
                    String className = entry.getName().replace('/', '.');
                    classNames.add(className.substring(0, className.length() - type.length()));
                }
            }
            for (String item:classNames
                 ) {

                System.out.println(item);
            }
            return classNames.get(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Iterates through a Robot's JAR file, trying to find either a .team file or .properties file
     */
    public static boolean isRobotType(String file, String type){
        try(ZipInputStream zip = new ZipInputStream(new FileInputStream(file)))
        {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(type)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static RobotsDTO validateRobot(byte[] bytes, File serverFile, String tipo, int idParticipante, String originalFileName, String pathRobocode) throws FileNotFoundException {
        //se creó el archivo en el servidor?
        try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
            stream.close();
            String extension = (tipo.equals("robot") ? ".properties" : ".team");
            //valida que el JAR que se suba sea del tipo que se indica (team o individual)
            if(RobocodeUtils.isRobotType(serverFile.getPath().replace('\\', '/'), extension)) {
                //obtenemos el nombre que necesita robocode para buscarlo y cargarlo en la batalla
                String testRobot = RobocodeUtils.getRobotClassName(serverFile.getPath().replace('\\', '/'), extension);
                //la validación requiere que sean dos por lo menos dos robots
                testRobot += "," + testRobot;

                BattleRunner br = new BattleRunner(new Robocode(), "TESTNUEVO", true,
                        800, 800, testRobot, 1);

                br.prepareRobocode(pathRobocode, "xml");
                br.runBattle();
                Robots robot = new Robots();
                robot.setNombre(originalFileName.replace(".jar", ""));
                robot.setActivo(0);
                robot.setIdParticipante(idParticipante);
                return new RobotsDTO(robot.getNombre(), robot.getActivo(), robot.getIdParticipante());
            }else{
                log.error("El robot a subir no es del tipo: " + tipo);
                throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Hubo un error al cargar el robot: " + e);
        }
    }
}

/*
                    //se copian los datos del archivo a cargar en el archivo creado
                    try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                        stream.write(bytes);
                        stream.close();
                        tipo = (tipo == "robot" ? ".properties" : ".team");
                        //valida que el JAR que se suba sea del tipo que se indica (team o individual)
                        if(RobocodeUtils.isRobotType(serverFile.getPath().replace('\\', '/'), tipo)){
                            //obtenemos el nombre que necesita robocode para buscarlo y cargarlo en la batalla
                            String testRobot = RobocodeUtils.getRobotClassName(serverFile.getPath().replace('\\', '/'), tipo);
                            //la validación requiere que sean dos por lo menos dos robots
                            testRobot += "," + testRobot;

                            //System.out.println("NOOOO");
                            //System.out.println(sRobot + " " + serverFile);
                            BattleRunner br = new BattleRunner(new Robocode(), "TESTNUEVO", true,
                                    800, 800, testRobot, 1);

                            br.prepareRobocode("C:/robocode", "xml");
                            br.runBattle();
                            Robots robot = new Robots();
                            robot.setNombre(file.getOriginalFilename().replace(".jar", ""));
                            robot.setActivo(0);
                            robot.setIdParticipante(idParticipante);
                            return new RobotsDTO(robot.getNombre(), robot.getActivo(), robot.getIdParticipante());

                        }else{
                            log.error("El robot a subir no es del tipo: " + tipo);
                            throw new RobotValidationException("El tipo de robot elegido no coincide con el robot a cargar");
                        }
                    } catch (IOException e) {
                        throw new FileNotFoundException("Hubo un error al cargar el robot: " + e);
                    }*/
package com.mozcalti.gamingapp.utils;

import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RobocodeUtils {

    private RobocodeUtils() {throw new IllegalStateException("Robocode Utility class");}
    public static String getRobotClassName(String src, String type) throws IOException {
        ArrayList<String> classNames = new ArrayList<>();
        try(ZipSecureFile zipSecureFile = new ZipSecureFile(src)){
            Enumeration<? extends ZipEntry> entries = zipSecureFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();
                if (!ze.isDirectory() && ze.getName().endsWith(type)) {
                    String className = ze.getName().replace('/', '.');
                    classNames.add(className.substring(Numeros.CERO.getNumero(), className.length() - type.length()));
                }
            }
            return classNames.get(Numeros.CERO.getNumero());
        }catch (IOException e){
            throw new FileNotFoundException("No se abrir el archivo jar para obtener el class name.");
        }

    }

    public static boolean isRobotType(String src, String type) throws FileNotFoundException {
        try(ZipSecureFile zipSecureFile = new ZipSecureFile(src)) {
            Enumeration<? extends ZipEntry> entries = zipSecureFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();
                if (!ze.isDirectory() && ze.getName().endsWith(type)) {
                    return true;
                }
            }
            return false;
        }catch (IOException e){
            throw new FileNotFoundException("No se pudo corroborar que el archivo fuera de robot fuera equipo o individual.");
        }
    }

    public static List<Path> findByFileName(Path path, String fileName)
            throws IOException {

        List<Path> result;
        try (Stream<Path> pathStream = Files.find(path,
                Integer.MAX_VALUE,
                (p, basicFileAttributes) ->
                        p.getFileName().toString().equalsIgnoreCase(fileName))
        ) {
            result = pathStream.toList();
        }
        return result;
    }
}
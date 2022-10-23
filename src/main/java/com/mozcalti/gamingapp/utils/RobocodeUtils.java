package com.mozcalti.gamingapp.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RobocodeUtils {

    private RobocodeUtils(){
        throw new IllegalStateException("Robocode Utility class");
    }
    public static String getRobotClassName(String file, String type) throws FileNotFoundException {
        ArrayList<String> classNames = new ArrayList<>();
        log.error(file + " TEST");
        try(ZipInputStream zip = new ZipInputStream(new FileInputStream(file)))
        {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(type)) {
                    String className = entry.getName().replace('/', '.');
                    log.info(className);
                    classNames.add(className.substring(0, className.length() - type.length()));
                }
            }
            return classNames.get(0);
        } catch (IOException e) {
            throw new FileNotFoundException("No se encontró el robot a abrir." + e);
        }
    }

    public static boolean isRobotType(String file, String type) throws FileNotFoundException {
        try(ZipInputStream zip = new ZipInputStream(new FileInputStream(file)))
        {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(type)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new FileNotFoundException("El archivo de tu robot no se puede abrir." + e);
        }
    }
}

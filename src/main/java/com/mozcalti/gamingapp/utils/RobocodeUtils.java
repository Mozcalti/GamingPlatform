package com.mozcalti.gamingapp.utils;

import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

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
                    classNames.add(className.substring(0, className.length() - type.length()));
                }
            }
            return classNames.get(0);
        }catch (IOException e){
            throw new FileNotFoundException("No se pudo corroboar un className válido.");
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
}
package com.mozcalti.gamingapp.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RobocodeUtils {

    private RobocodeUtils() {
        throw new IllegalStateException("Robocode Utility class");
    }

    public static String getRobotClassName(String file, String type) throws IOException {
        ArrayList<String> classNames = new ArrayList<>();
        log.error(file + " TEST");
        if (validateZip(file)) {
            try (ZipInputStream zip = new ZipInputStream(new FileInputStream(file))) {
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
        return null;
    }

    public static boolean isRobotType(String file, String type) throws FileNotFoundException {
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(file))) {
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

    private static boolean validateZip(String file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            if(iterateZip(entries, zipFile, file)){
                return true;
            }
        } catch (IOException e) {
            throw new FileNotFoundException("No se pudo crear el archivo de prueba para validar al robot.");
        }
        return false;
    }

    private static boolean iterateZip(Enumeration<? extends ZipEntry> entries, ZipFile zipFile, String file)
            throws IOException {
        final int THRESHOLD_ENTRIES = 100;
        final int THRESHOLD_SIZE = 10000000;
        final double THRESHOLD_RATIO = 10;
        int totalSizeArchive = 0;
        int totalEntryArchive = 0;

        while (entries.hasMoreElements()) {
            ZipEntry ze = entries.nextElement();
            InputStream in = new BufferedInputStream(zipFile.getInputStream(ze));
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream("./output_onlyfortesting" + file + ".txt"))) {

                totalEntryArchive++;

                int nBytes = -1;
                byte[] buffer = new byte[2048];
                double totalSizeEntry = 0;

                while ((nBytes = in.read(buffer)) > 0) { // Compliant
                    out.write(buffer, 0, nBytes);
                    totalSizeEntry += nBytes;
                    totalSizeArchive += nBytes;

                    double compressionRatio = totalSizeEntry / ze.getCompressedSize();
                    if (compressionRatio > THRESHOLD_RATIO) {
                        // ratio between compressed and uncompressed data is highly suspicious, looks like a Zip Bomb Attack
                        return false;
                    }
                }

                if (totalSizeArchive > THRESHOLD_SIZE) {
                    // the uncompressed data size is too much for the application resource capacity
                    return false;
                }

                if (totalEntryArchive > THRESHOLD_ENTRIES) {
                    // too much entries in this archive, can lead to inodes exhaustion of the system
                    return false;
                }

            }catch (RuntimeException e){
                throw new FileNotFoundException("No se pudo crear el archivo de prueba para validar al robot.");
            }
        }
        return true;
    }
}
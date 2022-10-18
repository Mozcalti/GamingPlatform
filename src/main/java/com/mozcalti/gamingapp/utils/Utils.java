package com.mozcalti.gamingapp.utils;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface Utils {

    LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String encodeImageToString(String path);

}

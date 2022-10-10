package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateUtils {
    public static void isValidDate(String date, String pattern, String msjError) throws ValidacionException {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            fmt.parseDateTime(date);
        } catch (Exception e) {
           throw new ValidacionException(msjError);
        }

    }

}

package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    //private static final String LOCALE = "es_ES";

    public static void isValidDate(String fecha, String pattern, String msjError) throws ValidacionException {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            fmt.parseDateTime(fecha);
        } catch (Exception e) {
           throw new ValidacionException(msjError);
        }

    }
    public static void isDatesRangoValid(String fechaIni, String fechaFin, String pattern, String msjError) throws ValidacionException {

        Calendar calendarIni = getDateFormat(fechaIni, pattern);
        Calendar calendarFin = getDateFormat(fechaFin, pattern);

        if(calendarIni.after(calendarFin)) {
            throw new ValidacionException(msjError);
        }

    }
    public static Calendar getDateFormat(String fecha, String pattern) throws ValidacionException {

        DateTimeFormatter fmt;
        Calendar fechaCalendar = Calendar.getInstance();

        try {
            fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            fechaCalendar.setTime(fmt.parseDateTime(fecha).toDate());
        } catch (Exception e) {
            throw new ValidacionException("Error en el metodo getDateFormat(String fecha, String pattern)\n: " + e.getStackTrace());
        } finally {
            fmt = null;
        }

        return fechaCalendar;
    }
    public static String getDateFormat(Date date, String pattern) throws ValidacionException {
        try {
            if(date != null) {
                DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
                return fmt.print(new DateTime(date));
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ValidacionException("Error en el metodo: getDateFormat(Date date, String pattern)"
                    + "\nMensaje de error: " + e.getMessage(), e);
        }
    }
    public static boolean isHoursRangoValid(String horaIni, String horaFin,
                                         String horaIniValid, String horaFinValid,
                                         String formato) {

        StringBuilder sHoraIniValid = new StringBuilder("");
        StringBuilder sHoraFinValid = new StringBuilder("");
        Calendar calendarIni = getDateFormat(horaIni, formato);
        Calendar calendarFin = getDateFormat(horaFin, formato);
        sHoraIniValid.append(horaIni, 0, 11).append(horaIniValid);
        sHoraFinValid.append(horaFin, 0, 11).append(horaFinValid);
        Calendar calendarIniValid = getDateFormat(sHoraIniValid.toString(), formato);
        Calendar calendarFinValid = getDateFormat(sHoraFinValid.toString(), formato);

        if(calendarIni.before(calendarIniValid) || calendarFin.after(calendarFinValid)) {
            return false;
        } else {
            return true;
        }

    }

    public static Date addMinutos(Date date, int minutos) throws ValidacionException {
        if (date != null) {
            DateTime dateTime = new DateTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutos);
            return calendar.getTime();
        } else {
            throw new ValidacionException("El parametro date no puede ser null");
        }
    }

    public static String addMinutos(String fecha, String pattern, int minutos) throws ValidacionException {
        if (fecha != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            return getDateFormat(fmt.parseDateTime(fecha).plusMinutes(minutos).toDate(), pattern);
        } else {
            throw new ValidacionException("El parametro date no puede ser null");
        }
    }

}

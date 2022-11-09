package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class DateUtils {

    public static final java.time.format.DateTimeFormatter FORMATTER = java.time.format.DateTimeFormatter.ofPattern(Constantes.FECHA_HORA_PATTERN);

    public static final String PARAMETRO_NOTNULL = "El parametro date no puede ser null";

    public static String formatDate(LocalDateTime time){
        return FORMATTER.format(time);
    }

    public static Date toDate(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

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

        if (calendarIni.after(calendarFin)) {
            throw new ValidacionException(msjError);
        }

    }

    public static boolean isDatesRangoValid(String fechaIni, String fechaFin, String pattern) {

        boolean resultado = false;
        Calendar calendarIni = getDateFormat(fechaIni, pattern);
        Calendar calendarFin = getDateFormat(fechaFin, pattern);

        if (calendarFin.after(calendarIni) || calendarIni.equals(calendarFin)) {
            resultado = true;
        }

        return resultado;

    }

    public static Calendar getDateFormat(String fecha, String pattern) throws ValidacionException {

        DateTimeFormatter fmt;
        Calendar fechaCalendar = Calendar.getInstance();

        try {
            fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            fechaCalendar.setTime(fmt.parseDateTime(fecha).toDate());
        } catch (Exception e) {
            throw new ValidacionException("Error en el metodo getDateFormat(String fecha, String pattern)\n: " + e.getStackTrace());
        }

        return fechaCalendar;
    }

    public static String getDateFormat(Date date, String pattern) throws ValidacionException {
        try {
            if (date != null) {
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
                                            String horaValidar, String formato) {

        boolean resultado = false;
        Calendar calendarIni = getDateFormat(horaIni, formato);
        Calendar calendarFin = getDateFormat(horaFin, formato);
        Calendar calendarHoraValidar = getDateFormat(horaValidar, formato);

        boolean isEquals = calendarHoraValidar.equals(calendarIni) || calendarHoraValidar.equals(calendarFin);
        boolean isValid = calendarHoraValidar.after(calendarIni) && calendarHoraValidar.before(calendarFin);

        if (isEquals || isValid) {
            resultado = true;
        }

        return resultado;

    }

    public static boolean isHoursRangoValid(String horaIni, String horaFin,
                                            String horaIniValid, String horaFinValid,
                                            String formato) {

        StringBuilder sHoraIniValid = new StringBuilder();
        StringBuilder sHoraFinValid = new StringBuilder();
        Calendar calendarIni = getDateFormat(horaIni, formato);
        Calendar calendarFin = getDateFormat(horaFin, formato);
        sHoraIniValid.append(horaIni, 0, 11).append(horaIniValid);
        sHoraFinValid.append(horaFin, 0, 11).append(horaFinValid);
        Calendar calendarIniValid = getDateFormat(sHoraIniValid.toString(), formato);
        Calendar calendarFinValid = getDateFormat(sHoraFinValid.toString(), formato);

        boolean resultado = true;

        if (calendarIni.before(calendarIniValid) || calendarFin.after(calendarFinValid)) {
            resultado = false;
        }

        return resultado;

    }
    public static Date addMinutos(Date date, int minutos) throws ValidacionException {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutos);
            return calendar.getTime();
        } else {
            throw new ValidacionException(PARAMETRO_NOTNULL);
        }
    }

    public static String addMinutos(String fecha, String pattern, int minutos) throws ValidacionException {
        if (fecha != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            return getDateFormat(fmt.parseDateTime(fecha).plusMinutes(minutos).toDate(), pattern);
        } else {
            throw new ValidacionException(PARAMETRO_NOTNULL);
        }
    }

    public static String addDias(String fecha, String pattern, int dias) throws ValidacionException {
        if (fecha != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            return getDateFormat(fmt.parseDateTime(fecha).plusDays(dias).toDate(), pattern);
        } else {
            throw new ValidacionException(PARAMETRO_NOTNULL);
        }
    }

    public static String addSegundos(String fecha, String pattern, int segundos) throws ValidacionException {
        if (fecha != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern).withLocale(Locale.ENGLISH);
            return getDateFormat(fmt.parseDateTime(fecha).plusSeconds(segundos).toDate(), pattern);
        } else {
            throw new ValidacionException(PARAMETRO_NOTNULL);
        }
    }

}

package com.mozcalti.gamingapp.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public final class ObjectUtils {

    public static final String EMPTY = "";

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object.getClass().isPrimitive() || object instanceof CharSequence) {
            return EMPTY.equals(String.valueOf(object).trim());
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).size() == 0;
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).size() == 0;
        } else if (object instanceof Iterator) {
            return !((Iterator<?>) object).hasNext();
        } else if (object instanceof Enumeration) {
            return !((Enumeration<?>) object).hasMoreElements();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else if (object instanceof File) {
            if (((File) object).length() == 0) {
                return true;
            } else {
                return false;
            }
        }
        return EMPTY.equals(object.toString().trim());
    }

}

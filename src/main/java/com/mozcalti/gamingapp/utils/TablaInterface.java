package com.mozcalti.gamingapp.utils;

import com.mozcalti.gamingapp.model.Institucion;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TablaInterface {
    Specification<Institucion> containsTextInAttributes(String text, List<String> attributes);
}

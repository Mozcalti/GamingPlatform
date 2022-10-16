package com.mozcalti.gamingapp.model.batallas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BatallasDTO {

    private List<BatallaDTO> batallasResponse;

    public BatallasDTO() {
        this.batallasResponse = new ArrayList<>();
    }
}

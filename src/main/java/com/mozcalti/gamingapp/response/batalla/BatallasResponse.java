package com.mozcalti.gamingapp.response.batalla;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BatallasResponse {

    private List<BatallaResponse> batallasResponse;

    public BatallasResponse() {
        this.batallasResponse = new ArrayList<>();
    }
}

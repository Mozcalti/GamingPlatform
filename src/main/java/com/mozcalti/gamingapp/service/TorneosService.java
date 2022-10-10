package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.TorneosEntity;
import com.mozcalti.gamingapp.request.TorneoRequest;

import java.sql.SQLException;

public interface TorneosService extends GenericServiceAPI<TorneosEntity, Integer> {

    public void saveTorneo(TorneoRequest torneoRequest) throws SQLException, ValidacionException;

}

package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.model.Participante;
import com.mozcalti.gamingapp.model.dto.ParticipanteDTO;
import com.mozcalti.gamingapp.model.dto.TablaParticipantesDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ParticipantesService extends GenericServiceAPI<Participante, Integer> {
    List<ParticipanteDTO> cargarArchivo(MultipartFile file) ;
    List<Participante> guardarParticipantes(List<Participante> participantes);
    List<TablaParticipantesDTO> listaParticipantes(String cadena);
    TablaParticipantesDTO obtenerParticipante(Integer id);
    Participante guardarParticipante(ParticipanteDTO participanteDTO);

}

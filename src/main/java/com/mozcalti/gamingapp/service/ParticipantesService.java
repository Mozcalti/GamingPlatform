package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.model.dto.DetalleParticipanteDTO;
import com.mozcalti.gamingapp.model.dto.ParticipanteDTO;
import com.mozcalti.gamingapp.model.dto.TablaParticipantesDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ParticipantesService extends GenericServiceAPI<Participantes, Integer> {
    List<ParticipanteDTO> cargarArchivo(MultipartFile file) ;
    List<Participantes> guardarParticipantes(List<Participantes> participantes);
    List<TablaParticipantesDTO> listaParticipantes(String cadena);
    DetalleParticipanteDTO obtenerParticipante(Integer id);
    Participantes guardarParticipante(ParticipanteDTO participanteDTO);

    Participantes actualizarParticipante(DetalleParticipanteDTO participanteDTO);

}

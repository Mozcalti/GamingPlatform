import DialogTitle from "@mui/material/DialogTitle";
import {Divider, Grid, Stack, TextField} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import React, {useState} from "react";
import {Participante} from "./Participante.model";

function AddParticipante(props) {
    const [participante, setParticipante] = useState(new Participante('', '', '', '', '', '', '', '', '', '', ''));
    const [open, setOpen] = useState(false);

    const abrirModal = (data) => {
        setParticipante(data)
        setOpen(true);
    };

    const cerrarModal = () => {
        setOpen(false);
    };

    const guardarParticipante = () => {
        props.addParticipante(participante);
        cerrarModal();
    }


    return (
        <div>
            <Button variant="contained" size="large"
                    onClick={() => abrirModal(new Participante('', '', '', '', '', '', '', '', '', '', ''))}>Agregar</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Nuevo Participante</DialogTitle>
                <Divider/>
                <DialogContent>
                    <Stack spacing={2}>
                        <TextField
                            autoFocus
                            label="Nombre"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.nombre}
                            onChange={(e) => setParticipante({...participante, nombre: e.target.value})}
                        />

                        <TextField
                            autoFocus
                            label="Apellidos"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.apellidos}
                            onChange={(e) => setParticipante({...participante, apellidos: e.target.value})}
                        />

                        <TextField
                            autoFocus
                            label="Correo"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.correo}
                            onChange={(e) => setParticipante({...participante, correo: e.target.value})}
                        />

                        <TextField
                            autoFocus
                            label="Carrera"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.carrera}
                            onChange={(e) => setParticipante({...participante, carrera: e.target.value})}
                        />

                        <TextField
                            autoFocus
                            label="Academia"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.academia}
                            onChange={(e) => setParticipante({...participante, academia: e.target.value})}
                        />
                        <TextField
                            autoFocus
                            label="IES"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.ies}
                            onChange={(e) => setParticipante({...participante, ies: e.target.value})}
                        />

                        <TextField
                            autoFocus
                            label="Semestre"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.semestre}
                            onChange={(e) => setParticipante({...participante, semestre: e.target.value})}
                        />
                        <TextField
                            autoFocus
                            label="Institución"
                            variant="outlined"
                            required
                            fullWidth
                            value={participante.idInstitucion}
                            onChange={(e) => setParticipante({...participante, idInstitucion: e.target.value})}
                        />
                    </Stack>

                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cerrar</Button>
                    <Button variant="contained" onClick={guardarParticipante}>Guardar</Button>
                </DialogActions>
            </Dialog>
        </div>


)

};

export default AddParticipante;
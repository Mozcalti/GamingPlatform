import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import ParticipantesService from "./Participantes.service"
import {DataGrid} from "@mui/x-data-grid";
import {
    Container,
    Divider,
    Grid,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import Button from "@mui/material/Button";
import * as PropTypes from "prop-types";
import {Participante} from "./Participante.model";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import AddParticipante from "./AddParticipante";


function Item(props) {
    return null;
}

Item.propTypes = {children: PropTypes.node};
const ParticipantesList = () => {
    const [participantes, setParticipantes] = useState([]);
    const [participante, setParticipante] = useState(new Participante('','','','','','','','','','',''));
    const [message, setMessage] = useState("");
    const [resultado, setResultado] = useState({
            success: false,
            error: false
        }
    );
    const [open, setOpen] = useState(false);

    const getParticipantes = (texto) => {
        ParticipantesService.lista(texto)
            .then(
                (response) => {
                    setParticipantes(response.data);
                },
                error => {
                    console.log(error)
                }
            );
    }

    const getParticipante = (id) => {
        ParticipantesService.getParticipante(id)
            .then(
                (response) => {
                    setParticipante(response.data);
                    abrirModal(response.data)
                },
                error => {
                    console.log(error)
                }
            );
    }

    const agregarParticipante = (participante) => {
        ParticipantesService
            .guardarParticipante(participante)
            .then(
                (response) => {
                    setResultado({...resultado, success: true})
                    getParticipantes('');
                },
                error => {
                    console.error(error);
                    setResultado({...resultado, error: true})
                }
            );
    }

    useEffect(() => {
        getParticipantes("")
    }, []);


    const abrirModal = (data) => {
        setParticipante(data)
        setOpen(true);
    };

    const cerrarModal = () => {
        setOpen(false);
    };


    const columns = [
        {field: 'nombre', headerName: 'Nombre', width: 300},
        {field: 'apellidos', headerName: 'Apellidos', width: 300},
        {field: 'fechaCreacion', headerName: 'Fecha de Creación', width: 300},
        {
            field: 'idParticipante', headerName: 'Acciones', width: 225, renderCell: (p) => {
                return (
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <Button variant="contained" onClick={() => getParticipante(p.value)}>Ver</Button>
                        </Grid>
                    </Grid>
                )

            }
        }];

    return (
        <>
            <ResponsiveAppBar/>
            <br/>
            <Container fluid>
                <Typography variant="h4">Participantes</Typography>
                <Divider/>
                <br/>
                <div style={{height: 430}}>
                    <Grid container spacing={2}>
                        <Grid item xs={6} md={6}>
                            <TextField label="Busqueda" variant="outlined" fullWidth
                                       onChange={(e) => setMessage(e.target.value)}/>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <Button onClick={() => getParticipantes(message)} variant="contained" size="large">Buscar</Button>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <AddParticipante addParticipante={agregarParticipante}/>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <Button variant="contained" size="large">Subir Archivo</Button>
                        </Grid>

                    </Grid>
                    <br/>
                    <DataGrid
                        rows={participantes}
                        columns={columns}
                        getRowId={row => row.correo}
                        initialState={{sorting: {sortModel: [{field: 'nombre', sort: 'asc',},],},}}
                        pageSize={50}
                        rowsPerPageOptions={[50]}
                        pagination
                    />
                </div>
            </Container>

            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Detalle del Participante</DialogTitle>
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
                            value={participante.institucion}
                            onChange={(e) => setParticipante({...participante, institucion: e.target.value})}
                        />
                    </Stack>

                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cerrar</Button>
                </DialogActions>
            </Dialog>
        </>
    )
};


export default ParticipantesList;
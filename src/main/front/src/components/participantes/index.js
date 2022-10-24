import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import ParticipantesService from "./Participantes.service"
import {DataGrid} from "@mui/x-data-grid";
import {
    Alert,
    Container,
    Divider,
    Grid, Snackbar,
    TextField,
    Typography
} from "@mui/material";
import Button from "@mui/material/Button";
import AddParticipante from "./AddParticipante";
import DetalleParticipante from "./DetalleParticipante";
import InstitucionService from "../../services/institucion.service";
import * as Yup from "yup";



const ParticipantesList = () => {
    const [participantes, setParticipantes] = useState([]);
    const [message, setMessage] = useState("");
    const [instituciones, setInstituciones] = useState([]);
    const [resultado, setResultado] = useState({
            success: false,
            error: false
        }
    );
    const [errorResponse, setErrorResponse] = useState("");

    const getParticipantes = (texto) => {
        ParticipantesService.lista(texto)
            .then(
                (response) => {
                    console.log(response.data)
                    setParticipantes(response.data);
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
                    console.error(error.response.data.message);
                    setErrorResponse(error.response.data.message)
                    setResultado({...resultado, error: true})
                }
            );
    }

    const actualizarParticipante = (participante) => {
        ParticipantesService
            .actualizarParticipante(participante)
            .then(
                (response) => {
                    setResultado({...resultado, success: true})
                    getParticipantes('');
                },
                error => {
                    console.error(error.response.data.message);
                    setErrorResponse(error.response.data.message)
                    setResultado({...resultado, error: true})
                }
            );
    }

    const getInstituciones = () => {
        InstitucionService.getInstituciones()
            .then(
                (response) => {
                    setInstituciones(response.data);
                },
                error => {
                    console.log(error)
                }
            );
    }
    useEffect(() => {
        getParticipantes("")
        getInstituciones()

    }, []);

    const ValidaForm = Yup.object().shape({
        nombre: Yup.string()
            .required('El nombre es obligatorio')
            .max(255, 'El nombre no debe exceder los 255 caracteres')
            .min(3, 'El nombre debe tener como minimo 3 caracteres'),
        apellidos: Yup.string()
            .required('El apellido es obligatorio')
            .max(255, 'El apellido no debe exceder los 255 caracteres')
            .min(3, 'El apellido debe tener como minimo 3 caracteres'),
        correo: Yup.string()
            .required('El correo es obligatorio')
            .email('El formato del correo no es correcto'),
        carrera: Yup.string()
            .required('La carrera es obligatoria')
            .max(255, 'La carrera no debe exceder los 255 caracteres')
            .min(3, 'La carrera debe tener como minimo 3 caracteres'),
        academia: Yup.string()
            .required('La academia es obligatoria')
            .max(255, 'La academia no debe exceder los 255 caracteres')
            .min(3, 'La academia debe tener como minimo 3 caracteres'),
        ies: Yup.string()
            .required('La IES es obligatoria')
            .max(255, 'La IES no debe exceder los 255 caracteres')
            .min(3, 'La IES debe tener como minimo 3 caracteres'),
        semestre: Yup.number()
            .required('El semestre es obligatorio'),
        idInstitucion: Yup.string()
            .required('La institucion es obligatoria')
    });



    const columns = [
        {field: 'nombre', headerName: 'Nombre', filterable: false ,width: 300},
        {field: 'apellidos', headerName: 'Apellidos', filterable: false , width: 300},
        {field: 'fechaCreacion', headerName: 'Fecha de Creación', filterable: false , width: 300},
        {
            field: 'idParticipante', headerName: 'Acciones', filterable: false , width: 225, renderCell: (p) => {
                return (
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <DetalleParticipante id={p.value} instituciones={instituciones} ValidaForm={ValidaForm} actualizarParticipante={actualizarParticipante} />
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
                <div style={{height: 400}}>
                    <Grid container spacing={2}>
                        <Grid item xs={6} md={6}>
                            <TextField label="Busqueda" variant="outlined" fullWidth
                                       onChange={(e) => setMessage(e.target.value)}/>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <Button onClick={() => getParticipantes(message)} variant="contained" size="large">Buscar</Button>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <AddParticipante addParticipante={agregarParticipante} instituciones={instituciones} ValidaForm={ValidaForm}/>
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

            <Snackbar open={resultado.success} autoHideDuration={6000} onClose={() => {setResultado({...resultado, success: false})}}>
                <Alert severity="success" variant="filled">Se guardo correctamente el participante</Alert>
            </Snackbar>
            <Snackbar open={resultado.error} autoHideDuration={6000}  onClose={() => {setResultado({...resultado, error: false})}}>
                <Alert severity="error" variant="filled">{errorResponse}</Alert>
            </Snackbar>
        </>
    )
};


export default ParticipantesList;
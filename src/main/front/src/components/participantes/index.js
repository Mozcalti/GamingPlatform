import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import ParticipantesService from "./Participantes.service"
import {DataGrid} from "@mui/x-data-grid";
import {
    Alert, Avatar,
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
import CargaMasiva from "./CargaMasiva";
import moment from "moment/moment";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {DatePicker} from "@mui/x-date-pickers/DatePicker";

const ParticipantesList = () => {
    const [participantes, setParticipantes] = useState([]);
    const [message, setMessage] = useState("");
    const [fecha, setFecha] = useState(null);
    const [instituciones, setInstituciones] = useState([]);
    const [resultado, setResultado] = useState({
            success: false,
            error: false
        }
    );
    const [errorResponse, setErrorResponse] = useState("");

    const getParticipantes = (texto, fechaCreacion) => {
        if (fechaCreacion === '' || fechaCreacion === null)
            fechaCreacion = '';
        else
            fechaCreacion = moment(fechaCreacion).format("yyyy-MM-DD HH:mm:ss.SSSSSS")
        ParticipantesService.lista(texto, fechaCreacion)
            .then(
                (response) => {
                    setParticipantes(response.data);
                    setFecha(null);
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
                    getParticipantes('', '');
                },
                error => {
                    setErrorResponse(error.response.data.message)
                    setResultado({...resultado, error: true})
                }
            );
    }

    const validaExcel = (participante) => {
        ParticipantesService
            .validaExcel(participante)
            .then(
                (response) => {
                    setResultado({...resultado, success: true})
                    getParticipantes("")
                },
                error => {
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
        getParticipantes("", "")
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

    const ValidaFormCargaMasiva = Yup.object().shape({
        file: Yup.mixed()
            .test("file", "El archivo es obligatorio", (value) => {
                if (value.length > 0) return true; else return false;
            })
            .test("fileType", "El formato del archivo no es valido", (value) => {
                return value.length && ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"].includes(value[0].type)
            })
    });


    const columns = [
        {
            field: 'foto', headerName: 'Foto', filterable: false, width: 60, renderCell: (params) => {
                return (
                    <Avatar src={params.value} alt="Remy Sharp" sx={{width: 40, height: 40,}}/>
                )
            }
        },
        {field: 'nombre', headerName: 'Nombre', filterable: false, width: 300},
        {field: 'apellidos', headerName: 'Apellidos', filterable: false, width: 300},
        {
            field: 'fechaCreacion',
            headerName: 'Fecha de Creación',
            filterable: false,
            width: 300,
            renderCell: (params) => {
                return moment(params.value).format("DD/MM/yyyy HH:mm:SS a");
            }
        },
        {
            field: 'idParticipante', headerName: 'Acciones', filterable: false, width: 150, renderCell: (p) => {
                return (
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <DetalleParticipante id={p.value} instituciones={instituciones} ValidaForm={ValidaForm}
                                                 actualizarParticipante={actualizarParticipante}/>
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

                <Grid container spacing={2}>
                    <Grid item xs={8} md={8}>
                        <Typography variant="h4">Participantes</Typography>
                    </Grid>
                    <Grid item xs={2} md={2}>
                        <AddParticipante addParticipante={agregarParticipante} instituciones={instituciones}
                                         ValidaForm={ValidaForm}/>
                    </Grid>
                    <Grid item xs={2} md={2}>
                        <CargaMasiva validaExcel={validaExcel}
                                     ValidaFormCargaMasiva={ValidaFormCargaMasiva}></CargaMasiva>
                    </Grid>
                </Grid>
                <br/>
                <Divider/>
                <br/>
                <div style={{height: 400}}>
                    <Grid container spacing={2}>
                        <Grid item xs={6} md={6}>
                            <TextField label="Busqueda" variant="outlined" fullWidth
                                       onChange={(e) => setMessage(e.target.value)}/>

                        </Grid>
                        <Grid item xs={4} md={4}>
                            <LocalizationProvider dateAdapter={AdapterMoment}>
                                <DatePicker
                                    renderInput={(props) => <TextField {...props} />}
                                    inputFormat="DD/MM/YYYY"
                                    label="Fecha de creación"
                                    value={fecha}
                                    onChange={(e) => {
                                        setFecha(e)
                                    }}
                                />
                            </LocalizationProvider>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <Button onClick={() => getParticipantes(message, fecha)} variant="contained"
                                    size="large">Buscar</Button>
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

            <Snackbar open={resultado.success} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, success: false})
            }}>
                <Alert severity="success" variant="filled">Se guardo correctamente el participante</Alert>
            </Snackbar>
            <Snackbar open={resultado.error} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, error: false})
            }}>
                <Alert severity="error" variant="filled">{errorResponse}</Alert>
            </Snackbar>
        </>
    )
};


export default ParticipantesList;
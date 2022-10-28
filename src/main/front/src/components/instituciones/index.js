import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import {Avatar, Container, Divider, Grid, TextField, Typography} from "@mui/material";
import Button from "@mui/material/Button";
import {DataGrid} from "@mui/x-data-grid";
import InstitucionesService from "./Instituciones.service";
import DetalleInstitucion from "./DetalleInstitucion";
import * as Yup from "yup";
import moment from "moment/moment";

import {AdapterMoment} from '@mui/x-date-pickers/AdapterMoment';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {DatePicker} from "@mui/x-date-pickers/DatePicker";

const InstitucionesList = () => {
    const [instituciones, setInstituciones] = useState([]);
    const [message, setMessage] = useState("");
    const [fecha, setFecha] = useState(null);


    const getInstituciones = (texto, fechaCreacion, indice) => {
        if (fechaCreacion == '' || fechaCreacion == null)
            fechaCreacion = '';
        else
            fechaCreacion = moment(fechaCreacion).format("yyyy-MM-DD HH:mm:ss.SSSSSS")

        InstitucionesService.lista(texto, fechaCreacion, indice)
            .then(
                (response) => {
                    setInstituciones(response.data.lista);
                    setFecha(null)
                },
                error => {
                    console.log(error)
                }
            );
    }


    useEffect(() => {
        getInstituciones("", "", 0)
    }, []);


    const ValidaForm = Yup.object().shape({
        nombre: Yup.string()
            .required('El nombre es obligatorio')
            .max(255, 'El nombre no debe exceder los 255 caracteres')
            .min(3, 'El nombre debe tener como minimo 3 caracteres'),
        correo: Yup.string()
            .required('El correo es obligatorio')
            .email('El formato del correo no es correcto')
    });


    const columns = [
        {
            field: 'logo', headerName: 'Logo', filterable: false, width: 60, renderCell: (params) => {
                return (
                    <Avatar src={params.value} alt="Remy Sharp" sx={{width: 40, height: 40,}}/>
                );
            }
        },
        {field: 'nombre', headerName: 'Nombre', filterable: false, width: 300},
        {field: 'correo', headerName: 'Correo Electronico', filterable: false, width: 300},
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
            field: 'id', headerName: 'Acciones', filterable: false, width: 150, renderCell: (p) => {
                return (
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <DetalleInstitucion id={p.value} ValidaForm={ValidaForm}/>
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
                <Typography variant="h4">Instituciones</Typography>
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
                            <Button onClick={() => getInstituciones(message, fecha, 0)} variant="contained"
                                    size="large">Buscar</Button>
                        </Grid>
                    </Grid>
                    <br/>
                    <DataGrid
                        rows={instituciones}
                        columns={columns}
                        getRowId={row => row.correo}
                        initialState={{sorting: {sortModel: [{field: 'nombre', sort: 'asc',},],},}}
                        pageSize={50}
                        rowsPerPageOptions={[50]}
                        pagination
                    />
                </div>
            </Container>
        </>
    );
};


export default InstitucionesList;
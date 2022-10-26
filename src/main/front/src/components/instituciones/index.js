import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import {Container, Divider, Grid, TextField, Typography} from "@mui/material";
import Button from "@mui/material/Button";
import {DataGrid} from "@mui/x-data-grid";
import InstitucionesService from "./Instituciones.service";
import DetalleInstitucion from "./DetalleInstitucion";
import * as Yup from "yup";

const InstitucionesList = () => {
    const [instituciones, setInstituciones] = useState([]);
    const [message, setMessage] = useState("");


    const getInstituciones = (texto, indice) => {
        InstitucionesService.lista(texto,indice)
            .then(
                (response) => {
                    setInstituciones(response.data.lista);
                },
                error => {
                    console.log(error)
                }
            );
    }


    useEffect(() => {
        getInstituciones("",0)
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
        {field: 'nombre', headerName: 'Nombre', filterable: false ,width: 300},
        {field: 'correo', headerName: 'Correo Electronico', filterable: false , width: 300},
        {field: 'fechaCreacion', headerName: 'Fecha de Creación', filterable: false , width: 300},
        {
            field: 'id', headerName: 'Acciones', filterable: false , width: 225, renderCell: (p) => {
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
                            <TextField label="Busqueda" variant="outlined" fullWidth  onChange={(e) => setMessage(e.target.value)}/>
                        </Grid>
                        <Grid item xs={2} md={2}>
                            <Button onClick={() => getInstituciones(message,0)} variant="contained" size="large">Buscar</Button>
                        </Grid>
                        <Grid item xs={2} md={2}>
                        </Grid>
                        <Grid item xs={2} md={2}>
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
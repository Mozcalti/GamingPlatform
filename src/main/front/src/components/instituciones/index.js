import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import {DataGrid} from "@mui/x-data-grid";
import InstitucionesService from "./Instituciones.service";
import DetalleInstitucion from "./DetalleInstitucion";
import * as Yup from "yup";
import {
    Alert, Avatar,
    Container,
    Divider,
    Grid, Snackbar,
    TextField,
    Typography
} from "@mui/material";
import { CargaMasivaInstituciones } from "./CargaMasivaInstituciones";
const InstitucionesList = () => {
    const [instituciones, setInstituciones] = useState([]);
    const [message, setMessage] = useState("");
    const [resultado, setResultado] = useState({
        sucess: false,
        error: false
    })
    const [errorResponse, setErrorResponse] = useState("");
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

    const getInstitucionMasiva = (texto, indice) => {
        InstitucionesService.getInstitucion(texto,indice)
            .then(
                (response) => {
                    console.log(response.data);
                    setInstituciones(response.data);
                },
                error => {
                    console.log(error)
                }
            );
    }


    useEffect(() => {
        getInstitucionMasiva();
    }, []);
    
    const validaExcel =(data)=>{
        InstitucionesService.validaExcelInstitucion(data)
        .then((response)=>{
            setResultado({...resultado,sucess:true})
            setInstituciones(response.data);
        },
        error=>{
            setErrorResponse(error.response.data.message);
            setResultado({...resultado, error: true});
        }
        )
    }

    const ValidaFormCargaMasiva = Yup.object().shape({
        file: Yup.mixed()
            .test("file", "El archivo es obligatorio", (value) => {if (value.length > 0)return true;else return false;})
            .test("fileType", "El formato del archivo no es valido", (value) =>{
                return value.length && ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"].includes(value[0].type)
            })
    });


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
                            <CargaMasivaInstituciones 
                            validaExcel={validaExcel}
                            ValidaFormCargaMasiva={ValidaFormCargaMasiva}></CargaMasivaInstituciones>
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
            <Snackbar open={resultado.success} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, success: false})
            }}>
                <Alert severity="success" variant="filled">Se guardo correctamente las instituciones</Alert>
            </Snackbar>
            <Snackbar open={resultado.error} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, error: false})
            }}>
                <Alert severity="error" variant="filled">{errorResponse}</Alert>
            </Snackbar>
        </>
    );
};


export default InstitucionesList;
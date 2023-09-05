import React, {useEffect, useState} from "react";
import {DataGrid} from '@mui/x-data-grid';
import AddUser from "./AddUser";
import UsuariosService from "./usuarios.service";
import {Container, Alert, Grid, Snackbar, Typography, Divider} from "@mui/material";
import ResponsiveAppBar from "../appBar/AppBar";
import Button from "@mui/material/Button";
import usuariosService from "./usuarios.service";


const UsersList = () => {
    const [usuarios, setUsuarios] = useState([]);
    const [resultado, setResultado] = useState({
            success: false,
            error: false
        }
    );

    const enviarCredenciales = (usuario) => {
        usuariosService.reset(usuario.email)
            .then(
                (response) => {
                    console.log(response);
                },
                error => {
                    console.error(error)
                }
            );
    };
    const columns = [
        {field: 'nombre', headerName: 'Nombre', width: 200},
        {field: 'apellidos', headerName: 'Apellidos', width: 200},
        {field: 'email', headerName: 'Email', width: 200},
        {field: 'rol', headerName: 'Rol', width: 150},
        {
            field: 'resetPass', headerName: 'Acciones', filterable: false, width: 300, renderCell: (p) => {
                return (
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <Button variant="contained"  onClick={() => enviarCredenciales(p.row)}>Reset Password</Button>
                        </Grid>
                    </Grid>
                )

            }
        }
    ];

    const getUsuarios = () => {
        UsuariosService.list()
            .then(
                (response) => {
                    setUsuarios(response.data);
                },
                error => {
                    console.error(error)
                }
            );
    }

    useEffect(() => {
        getUsuarios()
    }, []);

    const addUsuario = (usuario) => {
        setResultado({...resultado, success: (!!usuario), error: (!usuario)})
        getUsuarios();
    }

    return (
        <>
            <ResponsiveAppBar/>
            <br/>
            <Container fluid>
                <Grid container spacing={2}>
                    <Grid item xs={10} md={10}>
                        <Typography variant="h4">Usuarios</Typography>
                    </Grid>
                    <Grid item xs={2} md={2}>
                        <AddUser addUsuario={addUsuario}/>
                    </Grid>
                </Grid>
                <br/>
                <Divider/>
                <br/>
                <div style={{height: 500, width: '100%'}}>
                    <DataGrid
                        rows={usuarios}
                        columns={columns}
                        getRowId={row => row.email}/>
                </div>

            </Container>
            <Snackbar
                open={resultado.success}
                autoHideDuration={6000}
                onClose={() => { setResultado({...resultado, success: false})
                }}>
                <Alert severity="success" variant="filled">El usuario se guardó de forma correcta</Alert>
            </Snackbar>
            <Snackbar
                open={resultado.error}
                autoHideDuration={6000}
                onClose={() => { setResultado({...resultado, error: false})
                }}>
                <Alert severity="error" variant="filled">Ocurrió un error al agregar al usuario</Alert>
            </Snackbar>
        </>
    )
};

export default UsersList;
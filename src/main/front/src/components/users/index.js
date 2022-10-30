import React, {useEffect, useState} from "react";
import {DataGrid} from '@mui/x-data-grid';
import AddUser from "./AddUser";
import UsuariosService from "./usuarios.service";
import {Alert, Snackbar} from "@mui/material";
import ResponsiveAppBar from "../appBar/AppBar";


const UsersList = () => {
    const [usuarios, setUsuarios] = useState([]);
    const [resultado, setResultado] = useState({
            success: false,
            error: false
        }
    );

    const columns = [
        {field: 'nombre', headerName: 'Nombre', width: 200},
        {field: 'apellidos', headerName: 'Apellidos', width: 200},
        {field: 'email', headerName: 'Email', width: 200},
        {field: 'rol', headerName: 'Rol', width: 150},];

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

            <AddUser addUsuario={addUsuario}/>
            <div style={{height: 500, width: '100%'}}>
                <DataGrid
                    rows={usuarios}
                    columns={columns}
                    getRowId={row => row.email}/>
            </div>
        </>
    )
};

export default UsersList;
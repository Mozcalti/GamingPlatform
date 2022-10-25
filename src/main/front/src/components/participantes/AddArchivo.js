import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import React, {useState} from "react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/dist/yup";
import TablaExcel from "./TablaExcel";

function AddArchivo(props) {
    const [open, setOpen] = useState(false);

    

    const {
        reset,
        handleSubmit,
    } = useForm({
        resolver: yupResolver(props.ValidaForm)
    });

    const abrirModal = () => {
        setOpen(true);
    };

    const cerrarModal = () => {
        reset();
        setOpen(false);
    };

    const guardarParticipante = data => {
        console.log(data)
        props.addParticipante(data);
        cerrarModal();
    }

    return (
        <div>
            <Button variant="contained" size="large"
                    onClick={() => abrirModal()}>Agregar Archivo</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Nuevo Participante</DialogTitle>
                <DialogContent>
                    <TablaExcel/>
                </DialogContent>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="contained" onClick={handleSubmit(guardarParticipante)}>Guardar</Button>
                </DialogActions>               
                
            </Dialog>
        </div>
    )

};
export default AddArchivo;
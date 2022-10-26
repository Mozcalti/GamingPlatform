import React, {useState} from "react";
import Button from "@mui/material/Button";
import DialogTitle from "@mui/material/DialogTitle";
import {Divider, Stack, Typography} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/dist/yup";

function CargaMasiva(props){
    const [open, setOpen] = useState(false);

    const {
        register,
        reset,
        handleSubmit,
        formState: {errors}
    } = useForm({
        resolver: yupResolver(props.ValidaFormCargaMasiva)
    });

    const abrirModal = () => {
        setOpen(true);
    };

    const cerrarModal = () => {
        reset();
        setOpen(false);
    };

    const guardar = (data) => {
        const formData = new FormData();
        formData.append("file", data.file[0]);
        props.validaExcel(formData)
        cerrarModal();
    }

    return (
        <div>
            <Button variant="contained" size="large" onClick={() => abrirModal()}>Subir Archivo</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Carga Masiva</DialogTitle>
                <Divider/>
                <DialogContent>
                    <form>
                        <Stack spacing={2}>
                            <input accept=".xlsx, .xls, .csv" id="excel" name="excel" type="file" {...register('file')} error={!!errors.file}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.file?.message}
                            </Typography>
                        </Stack>
                    </form>
                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="contained" onClick={handleSubmit(guardar)}>Guardar</Button>
                </DialogActions>
            </Dialog>
        </div>
    )
};

export default CargaMasiva;
import React, {useState} from "react";
import Button from "@mui/material/Button";
import DialogTitle from "@mui/material/DialogTitle";
import {Divider, FormControl, InputLabel, MenuItem, Select, Stack, Typography} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/dist/yup";

function AgregarRobot(props) {
    const [open, setOpen] = useState(false);
    let tipos = ["team", "robot"];

    const {
        register,
        reset,
        handleSubmit,
        formState: {errors}
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

    const guardarRobot = data => {
        const formData = new FormData();
        formData.append("idParticipante", props.idParticipante);
        formData.append("idEtapa", props.idEtapa);
        formData.append("tipo", data.tipo);
        formData.append("file", data.file[0]);
        props.addRobot(formData);
        cerrarModal();
    }

    return (
        <div>
            <Button variant="contained" size="large"
                    onClick={() => abrirModal()}>Nuevo Robot</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Nuevo Robot</DialogTitle>
                <Divider/>
                <DialogContent>
                    <form onReset>
                        <Stack spacing={2}>
                            <input accept=".jar" id="robot" name="robot" type="file"
                                   {...register('file')} error={!!errors.file}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.file?.message}
                            </Typography>
                            <FormControl>
                                <InputLabel id="tipo-label">Tipo</InputLabel>
                                <Select
                                    id="tipo"
                                    labelId="tipo-label"
                                    {...register('tipo')}
                                    error={!!errors.tipo}>
                                    <MenuItem disabled selected>Selecciona un tipo</MenuItem>
                                    {tipos.map((t) => (
                                        <MenuItem value={t}>{t}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.tipo?.message}
                            </Typography>
                        </Stack>
                    </form>
                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="contained" onClick={handleSubmit(guardarRobot)}>Guardar</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};
export default AgregarRobot;
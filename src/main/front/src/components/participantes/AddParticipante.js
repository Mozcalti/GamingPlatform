import DialogTitle from "@mui/material/DialogTitle";
import {
    Divider,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import React, {useState} from "react";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/dist/yup";




function AddParticipante(props) {
    const [open, setOpen] = useState(false);

    

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

    const guardarParticipante = data => {
        console.log(data)
        props.addParticipante(data);
        cerrarModal();
    }

    return (
        <div>
            <Button variant="contained" size="large"
                    onClick={() => abrirModal()}>Agregar</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Nuevo Participante</DialogTitle>
                <DialogContent>
                    <form onReset>
                        <Stack spacing={2}>
                            <TextField
                                autoFocus
                                label="Nombre"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('nombre')}
                                error={!!errors.nombre}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.nombre?.message}
                            </Typography>

                            <TextField
                                autoFocus
                                label="Apellidos"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('apellidos')}
                                error={!!errors.apellidos}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.apellidos?.message}
                            </Typography>

                            <TextField
                                autoFocus
                                label="Correo"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('correo')}
                                error={!!errors.correo}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.correo?.message}
                            </Typography>

                            <TextField
                                autoFocus
                                label="Carrera"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('carrera')}
                                error={!!errors.carrera}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.carrera?.message}
                            </Typography>

                            <TextField
                                autoFocus
                                label="Academia"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('academia')}
                                error={!!errors.academia}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.academia?.message}
                            </Typography>
                            <TextField
                                autoFocus
                                label="IES"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('ies')}
                                error={!!errors.ies}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.ies?.message}
                            </Typography>

                            <TextField
                                autoFocus
                                type='number'
                                label="Semestre"
                                variant="outlined"
                                required
                                fullWidth
                                {...register('semestre')}
                                error={!!errors.semestre}/>
                            <Typography variant="subtitle1" color="error.main">
                                {errors.semestre?.message}
                            </Typography>


                            <FormControl>
                                <InputLabel id="institucion-label">Institución</InputLabel>
                                <Select
                                    id="idInstitucion"
                                    labelId="institucion-label"
                                    {...register('idInstitucion')}
                                    error={!!errors.idInstitucion}>
                                    <MenuItem disabled selected>Selecciona una Institución</MenuItem>
                                    {props.instituciones.map((institucion) => (
                                        <MenuItem value={institucion.id}>{institucion.nombre}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>

                            <Typography variant="subtitle1" color="error.main">
                                {errors.idInstitucion?.message}
                            </Typography>
                        </Stack>
                    </form>
                </DialogContent>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="contained" onClick={handleSubmit(guardarParticipante)}>Guardar</Button>
                </DialogActions>               
                
            </Dialog>
        </div>
    )

};

export default AddParticipante;
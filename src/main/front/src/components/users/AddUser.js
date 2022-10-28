import React, {useState} from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {
    Alert,
    Backdrop,
    Button, CircularProgress,
    FormControl, Grid,
    InputLabel,
    MenuItem,
    Select,
    TextField, Typography,
} from "@mui/material";
import * as Yup from "yup";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import UsuariosService from "./usuarios.service";
import {AlertTitle} from "@mui/lab";

function AddUser(props) {

    const [open, setOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const formValidationSchema = Yup.object().shape({
        nombre: Yup.string()
            .required('El nombre es obligatorio')
            .max(50, 'El nombre no debe exceder los 50 caracteres'),
        apellidos: Yup.string()
            .required('El apellido es obligatorio')
            .max(50, 'El apellido no debe exceder los 50 caracteres'),
        email: Yup.string()
            .required('El email es obligatorio')
            .email('El formato de email no es correcto'),
    });

    const {
        register,
        reset,
        handleSubmit,
        formState: {errors}
    } = useForm({
        resolver: yupResolver(formValidationSchema)
    });

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        reset({
            nombre: '',
            apellidos: '',
            email: '',
            rol: 'STAFF',
        });
        setLoading(false);
        setErrorMessage("");
        setOpen(false);
    };

    const onSave = usuario => {
        setLoading(true);
        UsuariosService
            .add(usuario)
            .then(
                () => {
                    handleClose();
                    setLoading(false);
                    props.addUsuario(usuario);
                },
                error => {
                    setLoading(false);
                    setErrorMessage(error.response.data.message);
                    props.addUsuario();
                }
            );
    }

    return (
        <div>
            <Button variant="contained" color="primary" onClick={handleClickOpen}>Agregar Usuario</Button>
            <Dialog open={open} onClose={handleClose} fullWidth={true}>
                {errorMessage && <Alert severity="error">
                    <AlertTitle>Error</AlertTitle>
                    <strong>{errorMessage}</strong>
                </Alert>}
                <Backdrop
                    sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}
                    open={loading}
                >
                    <CircularProgress color="inherit"/>
                </Backdrop>
                <DialogTitle>Nuevo Usuario</DialogTitle>
                <DialogContent>
                    <Grid container direction="column" spacing={3}>
                        <Grid item>
                            <TextField
                                autoFocus
                                label="Nombre"
                                variant="filled"
                                required
                                fullWidth
                                {...register('nombre')}
                                error={!!errors.nombre}
                            />
                            <Typography variant="subtitle1" color="error.main">
                                {errors.nombre?.message}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <TextField
                                label="Apellidos"
                                variant="filled"
                                required
                                fullWidth
                                {...register('apellidos')}
                                error={!!errors.apellidos}
                            />
                            <Typography variant="subtitle1" color="error.main">
                                {errors.apellidos?.message}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <TextField
                                label="Email"
                                variant="filled"
                                type="email"
                                required
                                fullWidth
                                {...register('email')}
                                error={!!errors.email}
                            />

                            <Typography variant="subtitle1" color="error.main">
                                {errors.email?.message}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <FormControl variant="filled" fullWidth>
                                <InputLabel id="rol-label">Rol</InputLabel>
                                <Select
                                    labelId="rol-label"
                                    id="rol"
                                    defaultValue='STAFF'
                                    {...register('rol')}
                                    label="Rol"
                                >
                                    <MenuItem value="STAFF">Staff</MenuItem>
                                    <MenuItem value="TECNICO">Contacto Técnico</MenuItem>
                                </Select>
                            </FormControl>
                        </Grid>
                    </Grid>
                </DialogContent>

                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={handleClose}>
                        Cancelar
                    </Button>
                    <Button variant="contained" color="primary" onClick={handleSubmit(onSave)}>Guardar</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default AddUser;
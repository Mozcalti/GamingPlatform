import InstitucionesService from "./Instituciones.service";
import React, {useState} from "react";
import {Institucion} from "./Institucion.model";
import Button from "@mui/material/Button";
import DialogTitle from "@mui/material/DialogTitle";
import {
    Avatar,
    Divider,
    Grid,
    IconButton,
    Stack,
    TextField, Typography,
} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/dist/yup";

const DetalleInstitucion = (props) => {
    const [open, setOpen] = useState(false);
    const [institucion, setInstitucion] = useState(
        new Institucion('', '', '', '', '')
    );
    const abrirModal = () => {
        setOpen(true);
    };

    const cerrarModal = () => {
        reset();
        setOpen(false);
    };

    const {
        register,
        reset,
        handleSubmit,
        formState: {errors}
    } = useForm({
        resolver: yupResolver(props.ValidaForm)
    });


    const ActualizarInstitucion = data => {
        setInstitucion(new Institucion(
            institucion.id,
            data.nombre,
            data.correo,
            institucion.logo,
            institucion.fechaCreacion)
        )
        console.log(institucion)
        cerrarModal();
    }



    const getInstitucion = (id) => {
        InstitucionesService.getInstitucion(id)
            .then(
                (response) => {
                    setInstitucion(response.data);
                    abrirModal()
                },
                error => {
                    console.log(error)
                }
            );
    }

    const convertBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(file);

            fileReader.onload = () => {
                resolve(fileReader.result);
                setInstitucion({...institucion, logo: fileReader.result})
            };

            fileReader.onerror = (error) => {
                reject(error);
            };
        });
    };

    return (
        <div>
            <Button variant="contained" onClick={() => getInstitucion(props.id)}>Ver</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Detalle de Institución</DialogTitle>
                <Divider/>
                <DialogContent>
                    <Stack spacing={2}>
                        <Grid container spacing={2}>
                            <Grid item xs={2}>
                                <IconButton aria-label="upload picture" component="label">
                                    <input hidden accept="image/*" id="logo" name="logo" type="file"
                                           onChange={(e) => convertBase64(e.target.files[0])}/>
                                    <Avatar src={institucion.logo} alt="Remy Sharp"
                                            sx={{fontSize: "1.5rem", boxShadow: 1, width: 80, height: 80,}}/>
                                </IconButton>
                            </Grid>
                            <Grid item xs={10}>
                                <br/>
                                <TextField
                                    autoFocus
                                    label="Nombre"
                                    variant="outlined"
                                    required
                                    fullWidth
                                    value={institucion.nombre}
                                    {...register('nombre')}
                                    />
                            </Grid>
                        </Grid>
                        <br/>
                        <TextField
                            autoFocus
                            label="Correo Electrónico"
                            variant="outlined"
                            required
                            fullWidth
                            {...register('correo')}
                            value={institucion.correo}
                            error={!!errors.correo}
                            onChange={(e) => setInstitucion({...institucion, correo: e.target.value})}/>
                        <Typography variant="subtitle1" color="error.main">
                            {errors.correo?.message}
                        </Typography>
                    </Stack>

                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cerrar</Button>
                    <Button variant="contained" onClick={handleSubmit(ActualizarInstitucion)}>Actualizar</Button>

                </DialogActions>
            </Dialog>
        </div>
    );

}

export default DetalleInstitucion;
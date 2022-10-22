import DialogTitle from "@mui/material/DialogTitle";
import {
    Avatar,
    Divider,
    FormControl, Grid, IconButton,
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
import {Participante} from "./Participante.model";
import ParticipantesService from "./Participantes.service";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup/dist/yup";


function DetalleParticipante(props) {
    const [open, setOpen] = useState(false);
    const [participante, setParticipante] = useState(
        new Participante('', '', '', '', '', '', '', '', '', '', '')
    );

    const getParticipante = (id) => {
        ParticipantesService.getParticipante(id)
            .then(
                (response) => {
                    setParticipante(response.data);
                    abrirModal()

                },
                error => {
                    console.log(error)
                }
            );
    }


    const cerrarModal = () => {
        reset();
        setOpen(false);
    };

    const abrirModal = () => {
        setOpen(true);
    };

    const {
        register,
        reset,
        handleSubmit,
        formState: {errors}
    } = useForm({
        resolver: yupResolver(props.ValidaForm)
    });

    const ActualizarParticipante = data => {
        setParticipante(new Participante(
            participante.idParticipante,
            data.nombre,
            data.apellidos,
            data.correo,
            data.academia,
            data.ies,
            data.carrera,
            data.semestre,
            participante.foto,
            participante.fechaCreacion,
            data.idInstitucion)
        )
        props.actualizarParticipante(participante);
        cerrarModal();
    }

    const convertBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(file);

            fileReader.onload = () => {
                resolve(fileReader.result);
                setParticipante({...participante, foto: fileReader.result})
            };

            fileReader.onerror = (error) => {
                reject(error);
            };
        });
    };


    return (

        <div>
            <Button variant="contained" onClick={() => getParticipante(props.id)}>Ver</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Detalle del Participante</DialogTitle>
                <Divider/>
                <DialogContent>
                    <Stack spacing={2}>
                        <Grid container spacing={2}>
                            <Grid item xs={2}>
                                <IconButton aria-label="upload picture" component="label">
                                    <input hidden accept="image/*" id="foto" name="foto" type="file"
                                           onChange={(e) => convertBase64(e.target.files[0])}/>
                                    <Avatar src={participante.foto} alt="Remy Sharp" sx={{fontSize: "1.5rem",boxShadow: 1,width: 80,height: 80,}}/>
                                </IconButton>
                            </Grid>
                            <Grid item xs={10}>
                                <br/>
                                <TextField
                                    autoFocus
                                    label="Correo"
                                    variant="outlined"
                                    required
                                    fullWidth
                                    value={participante.correo}
                                    {...register('correo')}
                                />

                            </Grid>
                        </Grid>
                        <br/>
                        <TextField
                            autoFocus
                            label="Nombre"
                            variant="outlined"
                            required
                            fullWidth
                            {...register('nombre')}
                            value={participante.nombre}
                            error={!!errors.nombre}
                            onChange={(e) => setParticipante({...participante, nombre: e.target.value})}/>
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
                            value={participante.apellidos}
                            error={!!errors.apellidos}
                            onChange={(e) => setParticipante({...participante, apellidos: e.target.value})}/>
                        <Typography variant="subtitle1" color="error.main">
                            {errors.apellidos?.message}
                        </Typography>


                        <TextField
                            autoFocus
                            label="Carrera"
                            variant="outlined"
                            required
                            fullWidth
                            {...register('carrera')}
                            error={!!errors.carrera}
                            value={participante.carrera}
                            onChange={(e) => setParticipante({...participante, carrera: e.target.value})}/>
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
                            error={!!errors.academia}
                            value={participante.academia}
                            onChange={(e) => setParticipante({...participante, academia: e.target.value})}
                        />
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
                            error={!!errors.ies}
                            value={participante.ies}
                            onChange={(e) => setParticipante({...participante, ies: e.target.value})}
                        />
                        <Typography variant="subtitle1" color="error.main">
                            {errors.ies?.message}
                        </Typography>

                        <TextField
                            autoFocus
                            label="Semestre"
                            variant="outlined"
                            required
                            fullWidth
                            {...register('semestre')}
                            error={!!errors.semestre}
                            value={participante.semestre}
                            onChange={(e) => setParticipante({...participante, semestre: e.target.value})}
                        />
                        <Typography variant="subtitle1" color="error.main">
                            {errors.semestre?.message}
                        </Typography>

                        <FormControl>
                            <InputLabel id="institucion-label">Institución</InputLabel>
                            <Select
                                labelId="institucion-label"
                                id="select-institucion"
                                {...register('idInstitucion')}
                                error={!!errors.idInstitucion}
                                value={participante.idInstitucion}
                                onChange={(e) => setParticipante({...participante, idInstitucion: e.target.value})}>
                                {props.instituciones.map((institucion) => (
                                    <MenuItem value={institucion.id}>{institucion.nombre}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Typography variant="subtitle1" color="error.main">
                            {errors.idInstitucion?.message}
                        </Typography>


                    </Stack>

                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cerrar</Button>
                    <Button variant="contained" onClick={handleSubmit(ActualizarParticipante)}>Actualizar</Button>

                </DialogActions>
            </Dialog>
        </div>


    );
};

export default DetalleParticipante;
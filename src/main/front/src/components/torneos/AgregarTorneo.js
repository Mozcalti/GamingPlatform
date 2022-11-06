import Button from "@mui/material/Button";
import React, {useState} from "react";
import DialogTitle from "@mui/material/DialogTitle";
import {
    Divider,
    FormControl,
    Grid,
    InputLabel,
    MenuItem,
    Select, Stack,
    TextField,
} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import {DatePicker} from "@mui/x-date-pickers/DatePicker";
import {LocalizationProvider} from "@mui/x-date-pickers/LocalizationProvider";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import ControlPointIcon from '@mui/icons-material/ControlPoint';
import {TorneoModel} from "./Torneo.model";
import moment from "moment/moment";

function AgregarTorneo(props) {
    let numeroEtapas = [2];
    const [open, setOpen] = useState(false);
    const [fechaInicio, setFechaInicio] = useState(null);
    const [fechaTermino, setFechaTermino] = useState(null);
    const [listaFechaHabil, setListaFechaHabil] = useState([
        {horaIniHabil: "", horaFinHabil: ""}
    ])
    const [torneo, setTorneo] = useState(new TorneoModel("","",""))

    const horasInicioHabiles = ["09:00 AM", "02:00 PM"]
    const horasTerminoHabiles = ["01:00 PM", "06:00 PM"]


    const abrirModal = () => {
        setOpen(true);
    };
    const cerrarModal = () => {
        setOpen(false);
    };

    const guardarTorneo = data => {
        props.guardarTorneo({...data,...{"horasHabiles": listaFechaHabil}})
        cerrarModal();
    }

    const handleListaFechaHabilAdd = () => {
        setListaFechaHabil([...listaFechaHabil, {horaIniHabil: "", horaFinHabil: ""}])
    }

    const handleListaFechaHabilRemove = (index) => {
        const list = [...listaFechaHabil]
        list.splice(index, 1)
        setListaFechaHabil(list)
    }


    return (
        <div>
            <Button variant="contained" size="medium" onClick={() => abrirModal()}>Agregar Torneo</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Nuevo Torneo</DialogTitle>
                <Divider/>
                <DialogContent>
                    <form onReset>
                        <Grid container spacing={3}>
                            <Grid item xs={6} md={6}>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker
                                        renderInput={(props) => <TextField {...props} />}
                                        inputFormat="DD/MM/YYYY"
                                        label="Fecha de inicio"
                                        value={fechaInicio}
                                        onChange={(e) => {
                                            setTorneo({...torneo, fechaInicio: moment(e).format("DD/MM/YYYY")})
                                            setFechaInicio(e)
                                        }}
                                    />
                                </LocalizationProvider>

                            </Grid>
                            <Grid item xs={6} md={6}>
                                <LocalizationProvider dateAdapter={AdapterMoment}>
                                    <DatePicker
                                        renderInput={(props) => <TextField {...props} />}
                                        inputFormat="DD/MM/YYYY"
                                        label="Fecha de termino"
                                        value={fechaTermino}
                                        onChange={(e) => {
                                            setTorneo({...torneo, fechaFin:moment(e).format("DD/MM/YYYY")})
                                            setFechaTermino(e)
                                        }}
                                    />
                                </LocalizationProvider>
                            </Grid>
                        </Grid>
                        <br/>
                        <Grid container spacing={2}>
                            <Grid item xs={12} md={12}>
                                <Stack spacing={2}>
                                    <FormControl>
                                        <InputLabel id="outlined-age-native-simple">Etapas del torneo</InputLabel>
                                        <Select
                                            id="etapas"
                                            onChange={(e) => {
                                                setTorneo({...torneo, numEtapas: e.target.value})
                                            }}>
                                            <MenuItem disabled selected>Selecciona</MenuItem>
                                            {numeroEtapas.map((num) => (
                                                <MenuItem value={num}>{num}</MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                </Stack>
                            </Grid>
                        </Grid>
                        <br/>
                        {listaFechaHabil.map((singleFecha, index) => (
                            <div>
                                <Grid container spacing={2} key={index}>
                                    <Grid item xs={5} md={5}>
                                        <Stack spacing={2}>
                                            <FormControl>
                                                <InputLabel id="outlined-age-native-simple">Hora Inicio</InputLabel>
                                                <Select
                                                    id="hora-incio"
                                                    value={singleFecha.horaIniHabil}
                                                    onChange={(e) => {
                                                        const list = [...listaFechaHabil]
                                                        list[index]["horaIniHabil"] = e.target.value;
                                                        setListaFechaHabil(list);
                                                    }}

                                                    >
                                                    <MenuItem disabled selected>Selecciona</MenuItem>
                                                    {horasInicioHabiles.map((i) =>
                                                        <MenuItem value={i}>{i}</MenuItem>
                                                    )}
                                                </Select>
                                            </FormControl>
                                        </Stack>
                                    </Grid>
                                    <Grid item xs={5} md={5}>
                                        <Stack spacing={2}>
                                            <FormControl>
                                                <InputLabel id="outlined-age-native-simple">Hora Fin</InputLabel>
                                                <Select
                                                    id="hora-fin"
                                                    value={singleFecha.horaFinHabil}
                                                    onChange={(e) => {
                                                        const list = [...listaFechaHabil]
                                                        list[index]["horaFinHabil"] = e.target.value;
                                                        setListaFechaHabil(list);
                                                    }}>
                                                    <MenuItem disabled selected>Selecciona</MenuItem>
                                                    {horasTerminoHabiles.map((i) =>
                                                        <MenuItem value={i}>{i}</MenuItem>
                                                    )}
                                                </Select>
                                            </FormControl>
                                        </Stack>
                                    </Grid>
                                    {listaFechaHabil.length - 1 === index && listaFechaHabil.length < 2 && (
                                        <Grid item xs={2} md={2}>
                                            <Button color="success" onClick={handleListaFechaHabilAdd}>
                                                <ControlPointIcon></ControlPointIcon>
                                            </Button>
                                        </Grid>
                                    )}
                                    {listaFechaHabil.length > 1 && (
                                        <Grid item xs={2} md={2}>
                                            <Button color="error" onClick={() => handleListaFechaHabilRemove(index)}>
                                                <HighlightOffIcon></HighlightOffIcon>
                                            </Button>
                                        </Grid>
                                    )}

                                </Grid>
                                <br/>
                            </div>
                        ))}
                    </form>
                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="contained" onClick={() => guardarTorneo(torneo)}>Guardar</Button>
                </DialogActions>
            </Dialog>

        </div>
    );

}

export default AgregarTorneo;
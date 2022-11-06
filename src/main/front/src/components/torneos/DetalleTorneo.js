import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import {Divider, FormControl, Grid, InputLabel, MenuItem, Select, Stack, TextField} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import React, {useState} from "react";
import {TorneoModel} from "./Torneo.model";
import TorneoService from "./Torneo.service";


function DetalleTorneo(t) {
    let numeroEtapas = [2];
    const [open, setOpen] = useState(false);
    const [torneo, setTorneo] = useState(new TorneoModel("","",""))
    const [listaFechaHabil, setListaFechaHabil] = useState([
        {horaIniHabil: "", horaFinHabil: ""}
    ])
    const horasInicioHabiles = ["09:00 AM", "02:00 PM"]
    const horasTerminoHabiles = ["01:00 PM", "06:00 PM"]


    const getTorneo = (id) => {
        TorneoService.gettorneo(id)
            .then(
                (response) => {
                    console.log(torneo.horasHabiles)
                    setTorneo(response.data);
                    abrirModal()
                },
                error => {
                    console.log(error)
                }
            );
        abrirModal();
    }


    const cerrarModal = () => {
        setOpen(false);
    };

    const abrirModal = () => {
        setOpen(true);
    };

    const guardarTorneo = data => {
        console.log({...data,...{"horasHabiles": listaFechaHabil}})
        cerrarModal();
    }


    return (
        <div>
            <Button variant="contained" color="primary" onClick={() => getTorneo(t.id)}>Actualizar</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Detalle del Torneo</DialogTitle>
                <Divider/>
                <DialogContent>
                    <form onReset>
                        <Grid container spacing={3}>
                            <Grid item xs={6} md={6}>
                                <TextField
                                    autoFocus
                                    label="Fecha Inicio"
                                    variant="outlined"
                                    required
                                    fullWidth
                                    value={torneo.fechaInicio}/>
                            </Grid>
                            <Grid item xs={6} md={6}>
                                <TextField
                                    autoFocus
                                    label="Fecha Inicio"
                                    variant="outlined"
                                    required
                                    fullWidth
                                    value={torneo.fechaFin}/>
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
                                            value={torneo.numEtapas}
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
                        {torneo.horasHabiles?.map((singleFecha, index) => (
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
                                                    }}>
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
                                    {/*{listaFechaHabil.length - 1 === index && listaFechaHabil.length < 2 && (
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
                                    )}*/}

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

export default DetalleTorneo;
import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import {Alert, Card, CardActions, CardContent, Container, Divider, Grid, Snackbar, Typography} from "@mui/material";
import AgregarTorneo from "./AgregarTorneo";
import TorneoService from "./Torneo.service";
import Button from "@mui/material/Button";
import DetalleTorneo from "./DetalleTorneo";
import AgregarEtapas from "./etapas/AgregarEtapas";


const Torneos = () => {
    const [torneos, setTorneos] = useState([]);
    const [errorResponse, setErrorResponse] = useState("");
    const [successResponse, setSuccessResponse] = useState("");

    const [resultado, setResultado] = useState({
        sucess: false,
        error: false
    })


    const guardarTorneo = (torneo) => {
        TorneoService.guardarTorneo(torneo)
            .then(
                () => {
                    setSuccessResponse("Se guardo correctamente el torneo");
                    setResultado({...resultado, sucess: true})
                    consultarTorneos()
                },
                error => {
                    setErrorResponse(error.response.data.message);
                    setResultado({...resultado, error: true});
                }
            )
    }

    const consultarTorneos = () => {
        TorneoService.consultarTorneos()
            .then((response) => {
                    setTorneos(response.data)
                    setResultado({...resultado, sucess: true})
                },
                error => {
                    setErrorResponse(error.response.data.message);
                    setResultado({...resultado, error: true});
                }
            )
    }

    const elimiarTorneo = (idTorneo) => {
        TorneoService.elimiarTorneo(idTorneo)
            .then(
                () => {
                    setSuccessResponse("Se elimino correctamente el torneo");
                    setResultado({...resultado, success: true})
                    consultarTorneos()
                },
                error => {
                    setErrorResponse(error.response.data.message);
                    setResultado({...resultado, error: true});
                }
            )

    }

    useEffect(() => {
        consultarTorneos();
    }, []);


    return (
        <>
            <ResponsiveAppBar/>
            <br/>
            <Container fluid>
                <Grid container spacing={2}>
                    <Grid item xs={8} md={8}>
                        <Typography variant="h4">Torneos, Etapas y Reglas</Typography>
                    </Grid>
                    <Grid item xs={2} md={2}>
                    </Grid>
                    <Grid item xs={2} md={2}>
                        <AgregarTorneo guardarTorneo={guardarTorneo}/>
                    </Grid>
                </Grid>
                <br/>
                <Divider/>
                <br/>
                <Grid container spacing={2}>
                    {torneos.map((t) => (
                        <Grid item xs={4} md={4}>
                            <Card>
                                <CardContent>
                                    <Typography variant="body2" color="text.secondary">Fecha de Inicio: {t.fechaInicio}</Typography>
                                    <Typography variant="body2" color="text.secondary">Fecha de Termino: {t.fechaFin}</Typography>
                                    <Typography variant="body2" color="text.secondary">Número de etapas: {t.numEtapas}</Typography>
                                    <br/>
                                    <Grid item xs={12} md={12}>
                                        <Typography variant="body2" color="text.secondary"><strong>Horario</strong></Typography>
                                    </Grid>
                                    {t.horasHabiles?.map((i) =>
                                        <Grid container>
                                            <Grid item xs={6} md={6}>
                                                <Typography variant="body2" color="text.secondary">De {i.horaIniHabil} a {i.horaFinHabil}</Typography>
                                            </Grid>
                                        </Grid>

                                    )}
                                </CardContent>
                                <CardActions>
                                    <DetalleTorneo id={t.idTorneo}/>
                                    <Grid container spacing={2}>
                                        <Grid item xs={6} md={6}>
                                            <Button variant="contained" color="error" onClick={() => elimiarTorneo(t.idTorneo)}>Eliminar</Button>
                                        </Grid>
                                        <Grid item xs={6} md={6}>
                                            <AgregarEtapas/>
                                        </Grid>
                                    </Grid>
                                </CardActions>
                            </Card>
                        </Grid>
                    ))}
                    </Grid>

            </Container>
            <Snackbar open={resultado.success} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, success: true})
            }}>
                <Alert severity="success" variant="filled">{successResponse}</Alert>
            </Snackbar>
            <Snackbar open={resultado.error} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, error: false})
            }}>
                <Alert severity="error" variant="filled">{errorResponse}</Alert>
            </Snackbar>
        </>
    );
}

export default Torneos;
import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import {
    Typography,
    Container,
    Divider,
    Grid, Alert, Snackbar, CardContent, CardActions, Card
} from "@mui/material";
import AgregarRobot from "./AgregarRobot";
import * as Yup from "yup";
import RobotsService from "./Robots.service";
import Button from "@mui/material/Button";


const Robots = () => {
    const [robots, setRobots] = useState([]);
    const [resultado, setResultado] = useState({
            success: false,
            error: false
        }
    );
    const [errorResponse, setErrorResponse] = useState("");
    const [successResponse, setSuccessResponse] = useState("");

    const getRobots = (idParticipante) =>{
        RobotsService.lista(idParticipante)
            .then(
                (response) => {
                    setRobots(response.data)
                },
                error => {
                    console.log(error)
                }
            )
    }
    
    const elimiarRobot = (idRobot) => {
        RobotsService.eliminarRobot(idRobot)
            .then(
                () => {
                    setResultado({...resultado, success: true})
                    setSuccessResponse("Se elimino correctamente el Robot");
                    getRobots(24)
                },
                error => {
                    setErrorResponse(error.response.data.message)
                    setResultado({...resultado, error: true})
                }
            )
      
    }

    const activarRobot = (nombre, idEquipo) => {
        const formData = new FormData();
        formData.append("nombre", nombre);
        formData.append("idEquipo", idEquipo);
        RobotsService.seleccionarRobot(formData)
            .then(
                () => {
                    setResultado({...resultado, success: true})
                    setSuccessResponse("Se selecciono correctamente el Robot");
                    getRobots(24)
                },
                error => {
                    setErrorResponse(error.response.data.message)
                    setResultado({...resultado, error: true})
                }
            )
    }




    const agregarRobot = (robot) => {
        RobotsService
            .cargarRobot(robot)
            .then(
                () => {
                    setResultado({...resultado, success: true})
                    setSuccessResponse("Se guardo correctamente el Robot");
                    getRobots(24)
                },
                error => {
                    setErrorResponse(error.response.data.message)
                    setResultado({...resultado, error: true})
                }
            )
    }

    const ValidaForm = Yup.object().shape({
        file: Yup.mixed()
            .test("file", "El archivo es obligatorio", (value) => {
                if (value.length > 0) return true; else return false;
            })
            .test("fileType", "El formato del archivo no es valido", (value) => {
                return value.length && ["application/x-java-archive"].includes(value[0].type)
            }),
        tipo: Yup.string()
            .required('El tipo de robot es obligatorio')
    });


    useEffect(() => {
        getRobots(24)
    }, []);

    return (
        <>
            <ResponsiveAppBar/>
            <br/>
            <Container fluid>
                <Grid container spacing={2}>
                    <Grid item xs={8} md={8}>
                        <Typography variant="h4">Robots</Typography>
                    </Grid>
                    <Grid item xs={2} md={2}>
                    </Grid>
                    <Grid item xs={2} md={2}>
                        <AgregarRobot ValidaForm={ValidaForm} addRobot={agregarRobot} />
                    </Grid>
                </Grid>
                <br/>
                <Divider/>
                <br/>
                <Grid container spacing={2}>
                    {robots.map((r) => (
                        <Grid item xs={4} md={4}>
                            <Card>
                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="div">{r.nombre}</Typography>
                                    <Typography variant="body2" color="text.secondary">Class Name: {r.className}</Typography>
                                    <Typography variant="body2" color="text.secondary">Tipo: {r.tipo}</Typography>
                                    <Typography variant="body2" color="text.secondary">Equipo: {r.idEquipo}</Typography>
                                </CardContent>
                                <CardActions>
                                    {r.activo   ?
                                        <Button variant="text" color="success">Seleccionado</Button>
                                        : <Button variant="contained" onClick={() => activarRobot(r.nombre, r.idEquipo)}>Seleccionar</Button>
                                    }
                                    <Button variant="contained" color="error" onClick={() => elimiarRobot(r.idRobot)}>Eliminar</Button>
                                </CardActions>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            </Container>
            <Snackbar open={resultado.success} autoHideDuration={6000} onClose={() => {
                setResultado({...resultado, success: false})
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

export default Robots;
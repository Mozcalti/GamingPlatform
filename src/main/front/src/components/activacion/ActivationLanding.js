import {useNavigate} from "react-router-dom";
import RegistroService from "./registro.service";
import React, {useEffect, useState} from "react";
import {Button, Container, Grid, Paper, Typography} from "@mui/material";
import Box from "@mui/material/Box";
import {Undo} from "@mui/icons-material";


const ActivationLanding = () => {
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const validaToken = token => {
            RegistroService
                .verify(token)
                .then( () => {
                        navigate(`/activacion?token=${token}`);
                    },
                    error => {
                        setMessage(error.response.data);
                    })
        };

        const queryParams = new URLSearchParams(window.location.search);
        validaToken(queryParams.get("token"))
    }, []);

    return (<div>
        <Container maxWidth="sm">
            <Grid
                container
                spacing={2}
                direction="column"
                justifyContent="center"
                style={{minHeight: "100vh"}}
            >
                <Paper elelvation={2} sx={{padding: 5}}>
                    <Grid
                        container
                        direction="column"
                        justifyContent="center"
                        alignItems="center" spacing={5}
                    >
                        <Grid item >
                            <Box
                                component="img"
                                sx={{
                                    height: 233,
                                    width: 350,
                                    maxHeight: { xs: 233, md: 167 },
                                    maxWidth: { xs: 350, md: 250 },
                                }}
                                alt={message}
                                src="/img/token_error.svg"
                            />

                        </Grid>
                        <Grid item xs={8}>
                            <Typography  >
                                {message}
                            </Typography>
                        </Grid>
                        <Grid item >
                            <Button
                                onClick={() => { navigate("/")}}
                                variant="contained"
                                startIcon={<Undo />}
                                color="primary"  >
                                Ir al Inicio
                            </Button>
                        </Grid>
                    </Grid>
                </Paper>
            </Grid>

        </Container>
    </div>);

};

export default ActivationLanding;
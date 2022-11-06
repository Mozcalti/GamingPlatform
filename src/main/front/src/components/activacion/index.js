import {
    Container,
    Grid,
    Paper,
    TextField,
    Typography,
    IconButton,
    InputAdornment, Button,
} from "@mui/material";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import React, {useState} from "react";
import {LoadingButton} from "@mui/lab";
import {Send} from "@mui/icons-material";
import PasswordStrengthBar from "react-password-strength-bar";
import RegistroService from "./registro.service";
import {useNavigate, useSearchParams} from "react-router-dom";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";
import Box from "@mui/material/Box";

const ActivationForm = () => {
    const [queryParams] = useSearchParams()
    const token = queryParams.get("token")

    const [status, setStatus] = useState({
        pass: "",
        showPass: false,
        error: false,
        loading: false,
        enabled: false,
        dialogOpen: false
    });

    const navigate = useNavigate();

    const [usuario, setUsuario] = useState({});

    const handleSubmit = (e) => {
        e.preventDefault();
        setStatus({
            ...status,
            error: false,
            loading: true,
        });

        RegistroService
            .activate(token, status.pass)
            .then((response) => {
                    setUsuario(response.data);
                    setStatus({...status, dialogOpen: true})
                },
                error => {
                    console.error("error", error);
                    navigate(`/registro?token=${token}`);
                }
            );
    };

    const handleDialogClose = () => {
        navigate('/');
    }

    const handlePassVisibilty = () => {
        setStatus({
            ...status,
            showPass: !status.showPass,
        });
    };

    const validateScore = score => {
        setStatus({
            ...status,
            enabled: (score < 3)
        });
    }

    return (
        <div>
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
                            alignItems="center"
                        >
                            <Box
                                component="img"
                                sx={{
                                    height: 233,
                                    width: 350,
                                    maxHeight: { xs: 233, md: 167 },
                                    maxWidth: { xs: 350, md: 250 },
                                }}
                                alt="Activa tu cuenta"
                                src="/img/token_password.svg"
                            />
                            <Typography component="h2" marginBottom={5} marginTop={4}>
                                Para poder activar tu cuenta debes crear una contraseña de acceso con un buen nivel de
                                seguridad.
                            </Typography>

                        </Grid>

                        <form onSubmit={handleSubmit}>
                            <Grid container direction="column" spacing={2}>

                                <Grid item>
                                    <TextField
                                        type={status.showPass ? "text" : "password"}
                                        fullWidth
                                        label="Contraseña"
                                        placeholder="Contraseña"
                                        variant="outlined"
                                        required
                                        onChange={(e) => setStatus({...status, pass: e.target.value})}
                                        InputProps={{
                                            endAdornment: (
                                                <InputAdornment position="end">
                                                    <IconButton
                                                        onClick={handlePassVisibilty}
                                                        aria-label="toggle password"
                                                        edge="end"
                                                    >
                                                        {status.showPass ? <VisibilityOffIcon/> : <VisibilityIcon/>}
                                                    </IconButton>
                                                </InputAdornment>
                                            ),
                                        }}
                                    />
                                    <PasswordStrengthBar password={status.pass}
                                                         scoreWords={['muy débil', 'débil', 'regular', 'bien', 'fuerte']}
                                                         shortScoreWord={'muy corta'} minLength={8}
                                                         onChangeScore={validateScore}/>
                                </Grid>

                                <Grid item>
                                    <LoadingButton
                                        disabled={status.enabled}
                                        type="submit"
                                        fullWidth
                                        endIcon={<Send/>}
                                        loading={status.loading}
                                        loadingPosition="end"
                                        variant="contained">
                                        Activar
                                    </LoadingButton>
                                </Grid>
                            </Grid>
                        </form>
                    </Paper>
                </Grid>
            </Container>


            <Dialog open={status.dialogOpen} fullWidth={true} onClose={handleDialogClose} >
                <DialogTitle>Cuenta activada</DialogTitle>
                <DialogContent>
                    <Grid container direction="column" spacing={3}  alignItems="center">
                        <Grid item>
                            <Box
                                component="img"
                                sx={{
                                    height: 233,
                                    width: 350,
                                    maxHeight: { xs: 233, md: 167 },
                                    maxWidth: { xs: 350, md: 250 },
                                }}
                                alt="Activa tu cuenta"
                                src="/img/token_ok.svg"
                            />
                            <Typography component="h3" marginTop={4}>
                                ¡Felicidades {usuario.nombre} tu cuenta ha sido activada!
                            </Typography>
                        </Grid>
                        <Grid item>
                            <Typography variant="subtitle1" >
                                Ahora puedes entrar al sistema usando tu correo de registro y tu contraseña.
                            </Typography>
                        </Grid>
                    </Grid>
                </DialogContent>

                <DialogActions>
                    <Button variant="contained" color="success" fullWidth onClick={handleDialogClose}>
                        Entrar
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default ActivationForm;
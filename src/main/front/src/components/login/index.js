import {
    Alert,
    Avatar,
    Container,
    Grid,
    Paper,
    TextField,
    Typography,
    IconButton,
    InputAdornment,
} from "@mui/material";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import React, {useState} from "react";
import {LoadingButton} from "@mui/lab";
import {useNavigate} from "react-router-dom";
import AuthService from "../../services/auth.service";

function SendIcon() {
    return null;
}

const Login = () => {
    const navigate = useNavigate();
    const [values, setValues] = useState({
        email: "",
        pass: "",
        showPass: false,
        error: false,
        loading: false,
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        setValues({
            ...values,
            error: false,
            loading: true,
        });

        AuthService.login(values.email, values.pass)
            .then(
            () => {
                setValues({
                    ...values,
                    error: false,
                    loading: false,
                });
                navigate("/mi-perfil")
            },
            error => {
                setValues({
                    ...values,
                    error: true,
                    loading: false,
                });
                console.error(error)
            }
        );
    };

    const handlePassVisibilty = () => {
        setValues({
            ...values,
            showPass: !values.showPass,
        });
    };

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
                                <Avatar sx={{ width: 70, height: 70 }} src="/img/robocode.jpg" />
                                <Typography component="h1" variant="h4" marginBottom={5}>
                                    Entrar
                                </Typography>

                        </Grid>

                        <form onSubmit={handleSubmit}>
                            <Grid container direction="column" spacing={2}>
                                <Grid item>
                                    <TextField
                                        type="email"
                                        required
                                        autoFocus
                                        autoComplete="email"
                                        label="Correo electrónico"
                                        placeholder="Correo electrónico"
                                        variant="outlined"
                                        fullWidth
                                        onChange={(e) => setValues({...values, email: e.target.value})}
                                    />
                                </Grid>

                                <Grid item>
                                    <TextField
                                        type={values.showPass ? "text" : "password"}
                                        fullWidth
                                        label="Contraseña"
                                        placeholder="Contraseña"
                                        variant="outlined"
                                        required
                                        onChange={(e) => setValues({...values, pass: e.target.value})}
                                        InputProps={{
                                            endAdornment: (
                                                <InputAdornment position="end">
                                                    <IconButton
                                                        onClick={handlePassVisibilty}
                                                        aria-label="toggle password"
                                                        edge="end"
                                                    >
                                                        {values.showPass ? <VisibilityOffIcon/> : <VisibilityIcon/>}
                                                    </IconButton>
                                                </InputAdornment>
                                            ),
                                        }}
                                    />
                                </Grid>

                                <Grid item>
                                    <LoadingButton
                                        type="submit"
                                        fullWidth
                                        endIcon={<SendIcon />}
                                        loading={values.loading}
                                        loadingPosition="end"
                                        variant="contained">
                                        Entrar
                                    </LoadingButton>
                                </Grid>

                                {values.error && <Grid item>
                                    <Alert severity="error">Nombre de usuario o contraseña incorrectos. Favor de verificar.</Alert>
                                </Grid>}

                            </Grid>
                        </form>
                    </Paper>
                </Grid>
            </Container>
        </div>
    );
};

export default Login;
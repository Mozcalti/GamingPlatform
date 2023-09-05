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
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import * as Yup from "yup";
import {Send} from "@mui/icons-material";

const Login = () => {
    const navigate = useNavigate();
    const [values, setValues] = useState({
        showPass: false,
        error: false,
        loading: false,
    });

    sessionStorage.removeItem("token");

    const formValidationSchema = Yup.object().shape({
        email: Yup.string()
            .required('El email es obligatorio')
            .email('El formato de email no es correcto'),
        password: Yup.string()
            .required('La contraseña es obligatoria')
    });

    const {
        register,
        handleSubmit,
        formState: {errors}
    } = useForm({
        resolver: yupResolver(formValidationSchema)
    });

    const onSubmit = data => {
        setValues({
            ...values,
            error: false,
            loading: true,
        });

        AuthService.login(data.email, data.password)
            .then(
                (response) => {
                    setValues({
                        ...values,
                        error: false,
                        loading: false,
                    });

                    if(response.rol === 'STAFF') {
                        navigate("/usuarios")
                    } else {
                        navigate('/robots')
                    }

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
                            <Avatar sx={{width: 70, height: 70}} src="/img/robocode.jpg"/>
                            <Typography component="h1" variant="h4" marginBottom={5}>
                                Entrar
                            </Typography>

                        </Grid>
                        <form onSubmit={handleSubmit(onSubmit)}>
                            <Grid container direction="column" spacing={3}>
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
                                        {...register('email')}
                                        error={!!errors.email}
                                    />
                                    <Typography variant="subtitle1" color="error.main">
                                        {errors.email?.message}
                                    </Typography>
                                </Grid>

                                <Grid item>
                                    <TextField
                                        type={values.showPass ? "text" : "password"}
                                        fullWidth
                                        label="Contraseña"
                                        placeholder="Contraseña"
                                        variant="outlined"
                                        required
                                        {...register('password')}
                                        error={!!errors.password}
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
                                    <Typography variant="subtitle1" color="error.main">
                                        {errors.password?.message}
                                    </Typography>
                                </Grid>

                                <Grid item>
                                    <LoadingButton
                                        type="submit"
                                        fullWidth
                                        endIcon={<Send/>}
                                        loading={values.loading}
                                        loadingPosition="end"
                                        variant="contained"
                                        onClick={handleSubmit(onSubmit)}
                                    >
                                        Entrar
                                    </LoadingButton>
                                </Grid>

                                {values.error && <Grid item>
                                    <Alert severity="error">Nombre de usuario o contraseña incorrectos. Favor de
                                        verificar.</Alert>
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
import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import {Divider} from "@mui/material";
import AuthService from "../../services/auth.service";
import {useLocation, useNavigate} from "react-router-dom";

const pages = [
    {title: 'Inicio', path: '/'},
    {title: 'Usuarios', path: '/usuarios'},
    {title: 'Instituciones', path: '/instituciones'},
    {title: 'Participantes', path: '/participantes'},
];

function ResponsiveAppBar() {

    const navigate = useNavigate();
    const location = useLocation();

    const pathname = location.pathname;


    const [anchorElNav, setAnchorElNav] = React.useState(null);

    const handleOpenNavMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    const handleLogout = () => {
        AuthService.logout();
        navigate('/');
    }

    return (
        <AppBar position="static" style={{background: '#fff'}}>
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <Typography
                        component="a"
                        href="https://plai.mx/"
                        sx={{
                            mr: 10,
                            display: {xs: 'none', md: 'flex'},
                        }}
                    >
                        <img src="/img/ui/logo_plai1.png" alt="Logo Plai" height={80}></img>
                    </Typography>

                    <Box sx={{flexGrow: 1, display: {xs: 'flex', md: 'none', color: '#000'}}}>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleOpenNavMenu}
                            color="inherit"
                        >
                            <MenuIcon/>
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorElNav}
                            anchorOrigin={{
                                vertical: 'bottom',
                                horizontal: 'left',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'left',
                            }}
                            open={Boolean(anchorElNav)}
                            onClose={handleCloseNavMenu}
                            sx={{
                                display: {xs: 'block', md: 'none', color: '#000'},
                            }}
                        >
                            {pages.map((page) => {
                                    const {title, path} = page;
                                    return (
                                        <MenuItem key={path} onClick={handleCloseNavMenu}>
                                            <Button
                                                key={path}
                                                onClick={handleCloseNavMenu}
                                                href={path}
                                                sx={{my: 1, color: 'black', display: 'block'}}>
                                                {title}
                                            </Button>
                                        </MenuItem>);
                                }
                            )}
                        </Menu>
                    </Box>

                    <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                        {pages.map((page) => {
                                const {title, path} = page;
                                return (
                                    <Divider key={path}>
                                        <MenuItem key={path} selected={path === pathname}>
                                            <Button
                                                key={path}
                                                onClick={handleCloseNavMenu}
                                                href={path}
                                                sx={{my: 1, color: 'black', display: 'block'}}>
                                                {title}
                                            </Button>
                                        </MenuItem>
                                    </Divider>
                                )
                            }
                        )}
                        <Divider>
                            <MenuItem>
                                <Button
                                    onClick={handleLogout}
                                    sx={{my: 1, color: 'black', display: 'block'}}>
                                    Cerrar Sesión
                                </Button>
                            </MenuItem>
                        </Divider>
                    </Box>
                    <Typography
                        component="a"
                        href="https://mozcalti.com/"
                        sx={{
                            display: {xs: 'none', md: 'flex'},
                        }}>
                        <img src="/img/ui/rojo_trans.png" alt="Logo MTI" height={80}></img>
                    </Typography>

                </Toolbar>
            </Container>

        </AppBar>
    );
}

export default ResponsiveAppBar;

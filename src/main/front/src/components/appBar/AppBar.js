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

const pages = ['Usuarios', 'Instituciones', 'Participantes', 'Cerrar Sesión'];

function ResponsiveAppBar() {
    const [anchorElNav, setAnchorElNav] = React.useState(null);

    const handleOpenNavMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    return (
        <AppBar position="static" style={{ background: '#fff'}}>
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <Typography
                        component="a"
                        href="/usuarios"
                        sx={{
                            mr: 10,
                            display: { xs: 'none', md: 'flex' },
                        }}
                    >
                        <img src="/img/ui/logo_plai1.png" alt="Logo Plai" width={180} height={80}></img>
                    </Typography>

                    <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none', color: '#000' } }}>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleOpenNavMenu}
                            color="inherit"
                        >
                            <MenuIcon />
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
                                display: { xs: 'block', md: 'none', color: '#000'},
                            }}
                        >
                            {pages.map((page) => (
                                <MenuItem key={page} onClick={handleCloseNavMenu}>
                                    <Button
                                        key={page}
                                        onClick={handleCloseNavMenu}
                                        href={page}
                                        sx={{ my: 1, color: 'black', display: 'block'}}>
                                        {page}
                                    </Button>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>

                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                        {pages.map((page) => (
                            <Divider>
                                <Button
                                    key={page}
                                    onClick={handleCloseNavMenu}
                                    href={page}
                                    sx={{ my: 1, color: 'black', display: 'block'}}>
                                    {page}
                                </Button>
                            </Divider>

                        ))}
                    </Box>
                    <Typography
                        component="a"
                        href="/"
                        sx={{
                            display: { xs: 'none', md: 'flex' },
                        }}>
                        <img src="/img/ui/rojo_trans.png" alt="Logo MTI" width={180} height={80}></img>
                    </Typography>

                </Toolbar>
            </Container>

        </AppBar>
    );
}
export default ResponsiveAppBar;

import React, {useState} from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {
    Button,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
} from "@mui/material";
import {Usuario} from "./Usuario.model";

function AddUser(props) {

    const [open, setOpen] = useState(false);
    const [usuario, setUsuario] = useState(new Usuario('', '', '', 'STAFF'));

    const handleClickOpen = () => {
        setUsuario({'rol': 'STAFF'});
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSave = () => {
        props.addUsuario(usuario);
        handleClose();
    }

    return (
        <div>
            <Button variant="contained" color="primary" onClick={handleClickOpen}>Agregar Usuario</Button>
            <Dialog open={open} onClose={handleClose} fullWidth={true}>
                <DialogTitle>Nuevo Usuario</DialogTitle>
                <DialogContent>

                    <Stack spacing={2}>
                        <TextField
                            autoFocus
                            label="Nombre"
                            variant="filled"
                            required
                            fullWidth
                            onChange={(e) => setUsuario({...usuario, nombre: e.target.value})}
                        />
                        <TextField
                            label="Apellidos"
                            variant="filled"
                            required
                            fullWidth
                            onChange={(e) => setUsuario({...usuario, apellidos: e.target.value})}
                        />
                        <TextField
                            label="Email"
                            variant="filled"
                            type="email"
                            required
                            fullWidth
                            onChange={(e) => setUsuario({...usuario, email: e.target.value})}
                        />

                        <FormControl variant="filled">
                            <InputLabel id="rol-label">Rol</InputLabel>
                            <Select
                                labelId="rol-label"
                                id="rol"
                                value={usuario.rol}
                                onChange={(e) => setUsuario({...usuario, rol: e.target.value})}
                                label="Rol"
                            >
                                <MenuItem value="STAFF">Staff</MenuItem>
                                <MenuItem value="TECNICO">Contacto Técnico</MenuItem>
                            </Select>
                        </FormControl>
                    </Stack>
                </DialogContent>


                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={handleClose}>
                        Cancelar
                    </Button>
                    <Button variant="contained" color="primary" onClick={handleSave}>Guardar</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default AddUser;
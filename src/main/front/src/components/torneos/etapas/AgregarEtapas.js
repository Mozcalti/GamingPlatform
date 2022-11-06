import Button from "@mui/material/Button";
import React, {useState} from "react";
import DialogTitle from "@mui/material/DialogTitle";
import {Divider, Grid} from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";

function AgregarEtapas(props) {
    const [open, setOpen] = useState(false);

    const abrirModal = () => {
        setOpen(true);
    };
    const cerrarModal = () => {
        setOpen(false);
    };

    return(
        <div>
            <Button variant="contained" size="medium" color="success" onClick={() => abrirModal()}>Etapas</Button>
            <Dialog open={open} onClose={cerrarModal} fullWidth={true}>
                <DialogTitle align="center">Etapas del Torneo</DialogTitle>
                <Divider/>
                <DialogContent>
                    <form onReset>
                        <Grid container spacing={3}>
                            <Grid item xs={6} md={6}>


                            </Grid>
                            <Grid item xs={6} md={6}>

                            </Grid>
                        </Grid>
                        <br/>

                    </form>
                </DialogContent>
                <Divider/>
                <DialogActions>
                    <Button variant="contained" color="inherit" onClick={cerrarModal}>Cancelar</Button>
                    <Button variant="contained" >Guardar</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default AgregarEtapas;
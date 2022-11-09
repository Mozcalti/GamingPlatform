import ResponsiveAppBar from "../appBar/AppBar";
import React, {useEffect, useState} from "react";
import {
    Typography,
    Container,
    Divider,
    Grid,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
} from "@mui/material";
import {DataGrid} from "@mui/x-data-grid";
import DashboardStaffService from "./DashboardStaff.service";
import Button from "@mui/material/Button";


const DashboardStaff = () => {
    const [etapas, setEtapas] = useState([]);
    const [etapa, setEtapa] = useState([]);
    const [batallas, setBatallas] = useState([]);
    const [resultadosGlobal, setResultadosGlobal] = useState([]);
    const [showEtapaInfo, setShowEtapaInfo] = useState(false);
    const [etapaSearchMethod, setEtapaSearchMethod] = useState("");
    const getEtapas = () => {
        DashboardStaffService.obtenerEtapas()
            .then(
                (response) => {
                    setEtapas(response.data);
                },
                error => {
                    console.log(error)
                }
            )
    }

    const getBatallasIndividuales = (idEtapa) =>{
        DashboardStaffService.listaBatallasIndividuales(idEtapa)
            .then(
                (response) => {
                    setShowEtapaInfo(true);
                    setEtapaSearchMethod("BATALLA");
                    setBatallas(response.data);
                },
                error => {
                    console.log(error)
                }
            )
    }

    const getDashboardGlobal = (idEtapa) =>{
        DashboardStaffService.listaResultadosGlobal(idEtapa)
            .then(
                (response) => {
                    setShowEtapaInfo(true);
                    setEtapaSearchMethod("GLOBAL");
                    setResultadosGlobal(response.data);
                    },
                error => {
                    console.log(error)
                }
            )
    }
    const gridStyle = {
        whiteSpace: 'normal',
        wordWrap: 'break-word'}

    useEffect(() => {
        getEtapas()
        setShowEtapaInfo(false);
    }, []);

    const columnsBatallas = [
        {field: 'rank', headerName: 'Rank', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'teamLeaderName', headerName: 'Robot', filterable: false, flex: 9, headerAlign: 'center', align: 'center'},
        {field: 'score', headerName: 'Score', filterable: false, flex: 2, headerAlign: 'center', align: 'center'},
        {field: 'survival', headerName: 'Survival', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'lastSurvivorBonus', headerName: 'Last Survivor Bonus', filterable: false, flex: 4, headerAlign: 'center', align: 'center'},
        {field: 'bulletDamage', headerName: 'Bullet Damage', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'bulletDamageBonus', headerName: 'Bullet Damage Bonus', filterable: false, flex: 4, headerAlign: 'center', align: 'center'},
        {field: 'ramDamage', headerName: 'Ram Damage', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'ramDamageBonus', headerName: 'Ram Damage Bonus', filterable: false, flex: 4, headerAlign: 'center',  align: 'center'},
        {field: 'firsts', headerName: '1sts', filterable: false, flex: 1, headerAlign: 'center', align: 'center'},
        {field: 'seconds', headerName: '2nds', filterable: false, flex: 1, headerAlign: 'center', align: 'center'},
        {field: 'thirds', headerName: '3rds', filterable: false, flex: 1, headerAlign: 'center', align: 'center'},
    ];

    const columnsResultadosGlobales = [
        {field: 'nombreInstitucion', headerName: 'Institución', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'nombreParticipantes', headerName: 'Participante', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'nombreRobot', headerName: 'Robot', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},
        {field: 'score', headerName: 'Puntuación', filterable: false, flex: 3, headerAlign: 'center', align: 'center'},

    ]
    return (
        <>
            <ResponsiveAppBar/>
            <br/>
            <Container fluid>
                <Grid container spacing={1}>
                    <Grid item xs={12} md={12}>
                        <Typography variant="h4">Dashboard</Typography>
                    </Grid>
                </Grid>
                <br/>
                <Divider/>
                <br/>
                <Grid container spacing={2}>
                    <Grid item xs={8} md={8}>
                        <FormControl fullWidth>
                            <InputLabel id="etapa-select-label">Etapa</InputLabel>
                            <Select
                                labelId="etapa-select-label"
                                id="etapa-select"
                                label="Etapa"
                                onChange={(e) => setEtapa(e.target.value)}
                            >
                                {etapas.map((e) =>(
                                    <MenuItem value={e.idEtapa}>Etapa {e.numeroEtapa} | Fecha Inicio: {e.fechaInicio} | Fecha Fin: {e.fechaFin}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={2} md={2}>
                        <Button onClick={() => getBatallasIndividuales(etapa)} variant="contained"
                                size="large">BUSCAR POR BATALLAS</Button>
                    </Grid>

                    <Grid item xs={2} md={2}>
                        <Button onClick={() => getDashboardGlobal(etapa)} variant="contained"
                                size="large">BUSCAR GLOBALMENTE</Button>
                    </Grid>
                </Grid>
                <br/>
                {showEtapaInfo ?
                    <Grid container spacing={2}>
                        {etapaSearchMethod.localeCompare("BATALLA") === 0 ?
                            <Grid container spacing={2}>
                                {batallas.length !== 0 ?
                                    <Grid item xs={12} md={12}>
                                        {batallas.map((b) => (
                                            <Grid item xs={12} md={12}>
                                                <Grid item xs={12} md={12} sx={{
                                                    backgroundColor: "#009294",
                                                    padding: "20px",
                                                    borderTopRightRadius: "10px",
                                                    color: "white"
                                                }}>
                                                    <Typography variant="h5" component="div">Batalla {b.idBatalla} |
                                                        Estado: {b.estatus} | Fecha: {b.fecha} |
                                                        Horario: {b.horaInicio} - {b.horaFin}</Typography>
                                                </Grid>
                                                <Grid item xs={12} md={12} sx={{
                                                    backgroundColor: "#8aac04",
                                                    padding: "20px",
                                                    color: "white"
                                                }}>
                                                    <Typography variant="h6"
                                                                component="div">Competidores: {b.reglasDTO.numCompetidores} |
                                                        Rondas: {b.reglasDTO.numRondas} |
                                                        Modalidad: {b.reglasDTO.trabajo} | Ancho de
                                                        Arena: {b.reglasDTO.arenaAncho} | Alto de
                                                        Arena: {b.reglasDTO.arenaAlto}</Typography>
                                                </Grid>
                                                {!b.estatus.localeCompare("TERMINADA") ?
                                                    <Grid item xs={12} md={12}>
                                                        <DataGrid rows={b.listaResultadosDTO} columns={columnsBatallas}
                                                                  getRowId={row => row.teamLeaderName} initialState={{
                                                            sorting: {
                                                                sortModel: [{
                                                                    field: 'rank',
                                                                    sort: 'asc',
                                                                },],
                                                            },
                                                        }} autoHeight={true} hideFooter={true} style={gridStyle}
                                                                  sx={{
                                                                      '& .MuiDataGrid-columnHeaderTitle': {
                                                                          textOverflow: "clip",
                                                                          whiteSpace: "break-spaces",
                                                                          lineHeight: 1,
                                                                          textAlign: "center"
                                                                      }
                                                                  }}
                                                        />
                                                        <br/>
                                                        <br/>
                                                    </Grid>
                                                    :
                                                    <Grid item xs={12} md={12} sx={{
                                                        padding: "20px",
                                                        display: "flex",
                                                        justifyContent: "center",
                                                        border: "1px solid lightGray",
                                                        borderRadius: "5px"
                                                    }}>
                                                        <Typography variant="h6" component="div">LA BATALLA SE
                                                            ENCUENTRA {b.estatus}</Typography>
                                                        <br/>
                                                    </Grid>
                                                }
                                            </Grid>
                                        ))}
                                    </Grid>
                                    :
                                    <Grid item xs={12} md={12} sx={{
                                        padding: "20px",
                                        display: "flex",
                                        justifyContent: "center",
                                        border: "1px solid lightGray",
                                        borderRadius: "5px",
                                        marginTop: "50px"
                                    }}>
                                        <Typography variant="h6" component="div">AUN NO HAY BATALLAS</Typography>
                                    </Grid>
                                }
                            </Grid>
                            : <Grid container spacing={2}>
                                {resultadosGlobal.length !== 0 ?
                                    <Grid item xs={12} md={12}>

                                        <Grid item xs={12} md={12} sx={{
                                            backgroundColor: "#009294",
                                            padding: "20px",
                                            borderTopRightRadius: "10px",
                                            color: "white"
                                        }}>
                                            <Typography variant="h5" component="div">RESULTADOS GLOBALES</Typography>
                                        </Grid>
                                            <Grid item xs={12} md={12}>
                                                    <Grid item xs={12} md={12}>
                                                        <DataGrid rows={resultadosGlobal} columns={columnsResultadosGlobales}
                                                                  getRowId={row => row.nombreRobot} initialState={{
                                                            sorting: {
                                                                sortModel: [{
                                                                    field: 'puntuacion',
                                                                    sort: 'asc',
                                                                },],
                                                            },
                                                        }} autoHeight={true} hideFooter={true} style={gridStyle}
                                                                  sx={{
                                                                      '& .MuiDataGrid-columnHeaderTitle': {
                                                                          textOverflow: "clip",
                                                                          whiteSpace: "break-spaces",
                                                                          lineHeight: 1,
                                                                          textAlign: "center"
                                                                      }
                                                                  }}
                                                        />
                                                        <br/>
                                                        <br/>
                                                    </Grid>
                                            </Grid>
                                    </Grid>
                                    :
                                    <Grid item xs={12} md={12} sx={{
                                        padding: "20px",
                                        display: "flex",
                                        justifyContent: "center",
                                        border: "1px solid lightGray",
                                        borderRadius: "5px",
                                        marginTop: "50px"
                                    }}>
                                        <Typography variant="h6" component="div">AUN NO HAY RESULTADOS GLOBALES</Typography>
                                    </Grid>
                                }
                            </Grid>
                        }
                    </Grid>
                    :
                    <Grid container spacing={2}>
                        <Grid item xs={12} md={12} sx={{padding: "20px", display: "flex", justifyContent: "center", border: "1px solid lightGray", borderRadius: "5px"}}>
                            <Typography variant="h6" component="div">BUSCA ALGUNA ETAPA</Typography>
                        </Grid>
                    </Grid>
                }
            </Container>
        </>
    );
}
export default DashboardStaff;
import './App.css';
import Login from './components/login';
import {HashRouter, Route, Routes} from "react-router-dom";
import Protected from "./components/protected";
import MiPerfil from "./components/perfil";
import UsersList from "./components/users";
import ActivationLanding from "./components/activacion/ActivationLanding";
import ActivationForm from "./components/activacion";
import ParticipantesList from "./components/participantes";
import InstitucionesList from "./components/instituciones";
import Robots from "./components/robots";
import DashboardStaff from "./components/dashboardStaff";
import DashboardParticipante from "./components/dashboardParticipante";
import Torneos from "./components/torneos";

function App() {
    return (
        <HashRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route path="/registro" element={<ActivationLanding/>}/>
                <Route path="/activacion" element={<ActivationForm/>}/>

                <Route
                    path="/mi-perfil"
                    element={
                        <Protected>
                            <MiPerfil/>
                        </Protected>
                    }
                />

                <Route
                    path="/usuarios"
                    element={
                        <Protected>
                            <UsersList/>
                        </Protected>
                    }/>

                <Route
                    path="/participantes"
                    element={
                        <Protected>
                            <ParticipantesList/>
                        </Protected>
                    }
                />
                <Route
                    path="/instituciones"
                    element={
                        <Protected>
                            <InstitucionesList />
                        </Protected>
                }/>

                <Route
                    path="/robots"
                    element={
                        <Protected>
                            <Robots />
                        </Protected>
                    }/>

                <Route
                    path="/dashboardStaff"
                    element={
                        <Protected>
                            <DashboardStaff />
                        </Protected>
                    }/>

                <Route
                    path="/dashboardParticipante"
                    element={
                        <Protected>
                            <DashboardParticipante />
                        </Protected>
                    }/>

                <Route
                    path="/torneos"
                    element={
                        <Protected>
                            <Torneos />
                        </Protected>
                    }/>

            </Routes>
        </HashRouter>
    );
}

export default App;

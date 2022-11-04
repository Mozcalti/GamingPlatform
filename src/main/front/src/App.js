import './App.css';
import Login from './components/login';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Protected from "./components/protected";
import MiPerfil from "./components/perfil";
import UsersList from "./components/users";
import ActivationLanding from "./components/activacion/ActivationLanding";
import ActivationForm from "./components/activacion";
import ParticipantesList from "./components/participantes";
import InstitucionesList from "./components/instituciones";
import Robots from "./components/robots";
import Batallas from "./components/dashboard";

function App() {
    return (
        <BrowserRouter>
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
                    path="/dashboard"
                    element={
                        <Protected>
                            <Batallas />
                        </Protected>
                    }/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;

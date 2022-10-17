import './App.css';
import Login from './componentes/login';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Protected from "./componentes/protected";
import MiPerfil from "./componentes/perfil";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>}/>
                <Route
                    path="/mi-perfil"
                    element={
                        <Protected>
                            <MiPerfil />
                        </Protected>
                    }
                />
            </Routes>
        </BrowserRouter>
    );
}

export default App;

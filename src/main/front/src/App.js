import './App.css';
import Login from './components/login';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Protected from "./components/protected";
import MiPerfil from "./components/perfil";

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

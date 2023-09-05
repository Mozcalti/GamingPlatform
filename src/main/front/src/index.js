import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080/'; // dev
//axios.defaults.baseURL = 'https://robocode.mozcalti.com'; // prod
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;

axios.interceptors.request.use(
    request => {
        if (!request.headers.Authorization && sessionStorage.getItem("token")) {
            request.headers.Authorization = sessionStorage.getItem("token");
        }

        return request;
    },
    error => Promise.reject(error)
);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

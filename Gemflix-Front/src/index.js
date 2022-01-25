import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app';
import axios from 'axios';
import AuthService from './service/auth_service';

const httpClient = axios.create({
  baseURL: 'http://localhost:9091'
})

const server = new AuthService(httpClient);

ReactDOM.render(
  <React.StrictMode>
    <App server={server}/>
  </React.StrictMode>,
  document.getElementById('root')
)
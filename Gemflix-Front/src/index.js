import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app';
import { Provider } from 'react-redux';
import { CookiesProvider } from "react-cookie";
import store from "./store/store";
import { persistStore } from 'redux-persist';
import { PersistGate } from 'redux-persist/integration/react';
import AuthService from './service/auth_service';
import setupInterceptors from './service/setup_interceptors';
import axios from 'axios';

function createInstance() {
  const httpClient = axios.create({
      baseURL: process.env.REACT_APP_API_URL,
      withCredentials: true,
      headers: {'Authorization': null}
  })
  return setupInterceptors(httpClient);
}

const httpClient = createInstance();
const server = new AuthService(httpClient);
const persistor = persistStore(store);

ReactDOM.render(
  <React.StrictMode>
    <CookiesProvider>
      <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
          <App server={server}/>
        </PersistGate>
      </Provider>
    </CookiesProvider>
  </React.StrictMode>,
  document.getElementById("root")
);
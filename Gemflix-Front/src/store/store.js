import { createStore, applyMiddleware } from 'redux';
import logger from 'redux-logger';
import reducers from './reducers';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

const persistConfig = {
    key: 'root',
    storage
};

const enhancedReducer = persistReducer(persistConfig, reducers);

export default createStore(enhancedReducer, applyMiddleware(logger));

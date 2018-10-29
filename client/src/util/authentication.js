import apiUrl from '../boot/constants';
import axios from 'axios';

export default function access() {
    const accessToken = localStorage.getItem('_access');
    const refreshToken = localStorage.getItem('_refresh');
    if (accessToken && refreshToken) {
        // todo: getting access token
    } else return null;
}

export function login(login, password) {
    // todo: call '/auth/login'
}

export function logout() {
    // todo: call '/auth/logout'
}

export function logoutAll() {
    // todo: call '/auth/logout-all'
}

function refresh(refreshToken, thenCallback, catchCallback) {
    const call = (callback, object) => {
        if (typeof callback === 'function') callback(object);
    };
    axios
        .get(apiUrl('/auth/refresh'), {params: {refreshToken: refreshToken}})
        .then(function (response) {
            set(response.data);
            console.debug('Authentication:', response);
            call(thenCallback, response.data)
        })
        .catch(function (error) {
            clear();
            const response = error.response ? error.response : error;
            console.warn('Authentication:', response);
            call(thenCallback, response)
        })
}

function set(data) {
    localStorage.setItem('_access', data.accessToken);
    localStorage.setItem('_refresh', data.refreshToken);
}

function clear() {
    localStorage.removeItem('_access');
    localStorage.removeItem('_refresh');
}
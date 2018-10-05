import {getApiUrl} from '../boot/constants'
import axios from 'axios';

/**
 * @param thenCallback {function(Object)}
 * @param catchCallback {function(Object)}
 */
export default function verify(thenCallback, catchCallback) {
    const accessToken = localStorage.getItem('token');
    if (accessToken !== null) {
        axios
            .get(getApiUrl('/auth/verify'), {params: {accessToken: accessToken}})
            .then(function (response) {
                login(response.data);
                console.log('Verify Response:', response);
                if (typeof thenCallback === 'function') thenCallback(response);
            })
            .catch(function (error) {
                logout();
                console.log('Verify Error:', ((error.response != null) ? error.response : error));
                if (typeof catchCallback === 'function') catchCallback(error);
            })
    }
}

export function login(token) {
    localStorage.setItem('token', (token.tokenType + ' ' + token.tokenHash));
    localStorage.setItem('login', token.login);
}

export function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('login');
}

export function getToken() {
    const token = localStorage.getItem('token');
    return (token != null) ? token : null;
}
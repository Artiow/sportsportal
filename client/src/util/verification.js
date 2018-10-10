import apiUrl from '../boot/constants';
import axios from 'axios';

/**
 * @param successCallback {function(Object)}
 * @param failureCallback {function(Object)}
 */
export default function verify(successCallback, failureCallback) {
    const accessToken = localStorage.getItem('token');
    const call = (callback, object) => {
        if (typeof callback === 'function') callback(object);
    };
    if (accessToken !== null) {
        axios
            .get(apiUrl('/auth/verify'), {params: {accessToken: accessToken}})
            .then(function (response) {
                login(response.data);
                console.log('Verify Response:', response.data);
                call(successCallback, response.data);
            })
            .catch(function (error) {
                logout();
                console.log('Verify Error:', ((error.response != null) ? error.response : error));
                call(failureCallback, error);
            })
    }
}

export function login(token) {
    localStorage.setItem('token', (token.tokenType + ' ' + token.tokenHash));
    localStorage.setItem('login', JSON.stringify(token.login));
}

export function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('login');
}

export function getToken() {
    const token = localStorage.getItem('token');
    return (token != null) ? token : null;
}

export function getLogin() {
    const login = localStorage.getItem('login');
    try {
        return JSON.parse(login);
    } catch (e) {
        return null;
    }
}
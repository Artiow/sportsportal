import apiUrl from '../boot/constants';
import axios from 'axios';
import qs from 'qs';

export default class Authentication {

    static register(body) {
        return new Promise((resolve, reject) => {
            axios
                .post(apiUrl('/auth/register'), body)
                .then(response => {
                    console.debug('Authentication', 'register', response);
                    const locationArray = response.headers.location.split('/');
                    resolve(locationArray[locationArray.length - 1]);
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'register', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }

    static initConfirmation(id, origin) {
        return new Promise((resolve, reject) => {
            axios
                .put(apiUrl(`/auth/confirm/${id}`), '', {
                    params: {origin: origin},
                    paramsSerializer: (params => qs.stringify(params))
                })
                .then(response => {
                    console.debug('Authentication', 'initConfirmation', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'initConfirmation', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }

    static doConfirmation(token) {
        return new Promise((resolve, reject) => {
            axios
                .put(apiUrl('/auth/confirm'), token)
                .then(response => {
                    console.debug('Authentication', 'doConfirmation', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'doConfirmation', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }

    static login(email, password) {
        return new Promise((resolve, reject) => {
            axios
                .get(apiUrl('/auth/login'), {
                    headers: {Authorization: `Basic ${window.btoa(email + ':' + password)}`}
                })
                .then(response => {
                    set(response.data);
                    console.debug('Authentication', 'login', response);
                    resolve(response.data.accessToken);
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'login', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }

    static access() {
        return new Promise((resolve, reject) => {
            const token = get();
            if (token) {
                if (isNotExpired(token.access)) {
                    resolve(token.access);
                } else if (isNotExpired(token.refresh)) {
                    this.refresh(token.refresh)
                        .then(data => resolve(data.accessToken))
                        .catch(error => {
                            console.error('Authentication', 'access', 'refresh error');
                            reject(error)
                        });
                } else {
                    console.warn('Authentication', 'access', 'stored tokens was expired');
                    reject(null);
                }
            } else {
                console.warn('Authentication', 'access', 'no stored tokens');
                reject(null);
            }
        });
    }

    static refresh(refreshToken) {
        return new Promise((resolve, reject) => {
            axios
                .get(apiUrl('/auth/refresh'), {
                    headers: {Authorization: `Bearer ${refreshToken}`}
                })
                .then(response => {
                    set(response.data);
                    console.debug('Authentication', 'refresh', response);
                    resolve(response.data);
                })
                .catch(error => {
                    clear();
                    const response = error.response;
                    console.error('Authentication', 'refresh', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }

    static logout() {
        return new Promise((resolve, reject) => {
            clear();
            resolve();
        });
    }
}

function isNotExpired(token) {
    const ALLOWABLE_DELAY_SEC = 90; // assumption by slow communication channel
    return Math.floor(Date.now() / 1000) < (parse(token).exp - ALLOWABLE_DELAY_SEC);
}

function parse(token) {
    return JSON.parse(window.atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
}

function get() {
    const access = localStorage.getItem('_access');
    const refresh = localStorage.getItem('_refresh');
    return (access && refresh) ? {access, refresh} : null;
}

function set(data) {
    localStorage.setItem('_access', data.accessToken);
    localStorage.setItem('_refresh', data.refreshToken);
}

function clear() {
    localStorage.removeItem('_access');
    localStorage.removeItem('_refresh');
}
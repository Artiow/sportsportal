import apiUrl from '../boot/constants';
import axios from 'axios';

export default class Authentication {

    static access() {
        return new Promise((resolve, reject) => {
            const token = get();
            if (token) {
                if (isNotExpired(token.access)) {
                    resolve(token.access);
                } else if (isNotExpired(token.refresh)) {
                    refresh(token.refresh)
                        .then(data => resolve(data.accessToken))
                        .catch(data => reject(data));
                } else {
                    clear();
                    console.warn('Authentication [access]: stored tokens was expired');
                    reject(null);
                }
            } else {
                console.warn('Authentication [access]: no stored tokens');
                reject(null);
            }
        });
    }

    static login(login, password) {
        return new Promise((resolve, reject) => {
            // todo: call '/auth/login'
        });
    }

    static logout() {
        return new Promise((resolve, reject) => {
            // todo: call '/auth/logout'
        });
    }

    static logoutAll() {
        return new Promise((resolve, reject) => {
            // todo: call '/auth/logout-all'
        });
    }

    static register() {
        return new Promise((resolve, reject) => {
            // todo: call '/auth/register
        });
    }
}

function isNotExpired(token) {
    const ALLOWABLE_DELAY_SEC = 150; // assumption by slow communication channel
    return Math.floor(Date.now() / 1000) < (parse(token).exp - ALLOWABLE_DELAY_SEC);
}

function parse(token) {
    return JSON.parse(window.atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
}

function refresh(refreshToken) {
    return new Promise((resolve, reject) => {
        axios
            .get(apiUrl('/auth/refresh'), {
                params: {refreshToken: refreshToken}
            })
            .then(response => {
                set(response.data);
                console.debug('Authentication [refresh]:', response);
                resolve(response.data);
            })
            .catch(error => {
                clear();
                const response = error.response;
                console.error('Authentication [refresh]:', response ? response : error);
                reject((response && response.data) ? response.data : null)
            })
    });
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
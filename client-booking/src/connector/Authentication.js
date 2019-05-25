import ParamsSerializer from '../util/connector/ParamsSerializer';
import Location from '../util/connector/Location';
import Headers from '../util/connector/Headers';
import API from '../boot/constants';
import axios from 'axios';

export default class Authentication {

    static register(body) {
        return new Promise((resolve, reject) => {
            axios
                .post(API.url('/auth/register'), body)
                .then(response => {
                    console.debug('Authentication', 'register', response);
                    resolve(Location.extractId(response.headers));
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'register', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }

    static initConfirmation(id, href) {
        return new Promise((resolve, reject) => {
            axios
                .put(API.url(`/auth/confirm/${id}`), '', {
                    paramsSerializer: ParamsSerializer.stringify(),
                    params: {href: href}
                })
                .then(response => {
                    console.debug('Authentication', 'initConfirmation', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'initConfirmation', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }

    static doConfirmation(token) {
        return new Promise((resolve, reject) => {
            axios
                .put(API.url('/auth/confirm'), '', {
                    paramsSerializer: ParamsSerializer.stringify(),
                    params: {token: token}
                })
                .then(response => {
                    console.debug('Authentication', 'doConfirmation', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'doConfirmation', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }

    static initRecovery(href, email) {
        return new Promise((resolve, reject) => {
            axios
                .put(API.url(`/auth/recovery-init`), {
                    email: email
                }, {
                    paramsSerializer: ParamsSerializer.stringify(),
                    params: {href: href}
                })
                .then(response => {
                    console.debug('Authentication', 'initRecovery', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'initRecovery', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }

    static doRecovery(token, password) {
        return new Promise((resolve, reject) => {
            axios
                .put(API.url('/auth/recovery-act'), {
                    password: password
                }, {
                    paramsSerializer: ParamsSerializer.stringify(),
                    params: {token: token}
                })
                .then(response => {
                    console.debug('Authentication', 'doRecovery', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'doRecovery', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }

    static login(email, password) {
        return new Promise((resolve, reject) => {
            axios
                .get(API.url('/auth/login'), Headers.basic(email, password))
                .then(response => {
                    set(response.data);
                    console.debug('Authentication', 'login', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.warn('Authentication', 'login', response ? response : error);
                    reject((response && response.data) ? response.data : error);
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
                    this.refresh(token.refresh).then(data => resolve(data.accessToken)).catch(error => reject(error));
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
                .get(API.url('/auth/refresh'), Headers.bearer(refreshToken))
                .then(response => {
                    set(response.data);
                    console.debug('Authentication', 'refresh', response);
                    resolve(response.data);
                })
                .catch(error => {
                    clear();
                    const response = error.response;
                    console.error('Authentication', 'refresh', response ? response : error);
                    reject((response && response.data) ? response.data : error);
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
    const ALLOWABLE_DELAY_SEC = 60; // assumption by slow communication channel
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
import Authentication from './Authentication';
import apiUrl from '../boot/constants';
import axios from 'axios';
import qs from "qs";

export default class Playground {

    static list(filter) {
        return new Promise((resolve, reject) => {
            axios
                .get(apiUrl('/playground/list'), {
                    paramsSerializer: params => qs.stringify(params, {arrayFormat: 'repeat'}),
                    params: filter
                })
                .then(response => {
                    console.debug('Playground', 'list', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Playground', 'list', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }

    static get(id) {
        return new Promise((resolve, reject) => {
            axios.get(apiUrl(`/playground/${id}`))
                .then(response => {
                    console.debug('Playground', 'get', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Playground', 'get', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                });
        });
    }

    static create(body) {
        return new Promise((resolve, reject) => {

        });
    }

    static update(id, body) {
        return new Promise((resolve, reject) => {

        });
    }

    static delete(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    axios
                        .delete(apiUrl(`/playground/${id}`), {
                            headers: {'Authorization': `Bearer ${token}`}
                        })
                        .then(response => {
                            console.debug('Playground', 'delete', response);
                            resolve();
                        })
                        .catch(error => {
                            const response = error.response;
                            console.error('Playground', 'delete', response ? response : error);
                            reject((response && response.data) ? response.data : null)
                        })
                })
                .catch(error => {
                    console.error('Playground', 'delete', 'access error');
                    reject(null);
                });
        });
    }

    static getShort(id) {
        return new Promise((resolve, reject) => {
            axios.get(apiUrl(`/playground/${id}/short`))
                .then(response => {
                    console.debug('Playground', 'short', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Playground', 'short', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                });
        });
    }

    static getBoard(id, from, to) {
        return new Promise((resolve, reject) => {
            axios
                .get(apiUrl(`/playground/${id}/board`), {
                    params: {from: from, to: to}
                })
                .then(response => {
                    console.debug('Playground', 'board', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Playground', 'board', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                });
        });
    }

    static doCheck(id, version, reservations) {
        return new Promise((resolve, reject) => {
            axios
                .get(apiUrl(`/playground/${id}/check`), {
                    paramsSerializer: params =>
                        qs.stringify(params, {arrayFormat: 'repeat'}),
                    params: {
                        version: version,
                        reservations: reservations
                    }
                })
                .then(response => {
                    console.debug('Playground', 'check', response);
                    const reservations = response.data.reservations;
                    reservations.forEach((value, index, array) => array[index] = value.replace(/:\d\d$/, ''));
                    resolve(reservations);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Playground', 'check', response ? response : error);
                    reject((response && response.data) ? response.data : null)
                });
        });
    }

    static doReserve(id, reservations) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    axios
                        .post(apiUrl(`/playground/${id}/reserve`), {
                            reservations: reservations
                        }, {
                            headers: {'Authorization': `Bearer ${token}`}
                        })
                        .then(response => {
                            console.debug('Playground', 'reserve', response);
                            const locationArray = response.headers.location.split('/');
                            resolve(locationArray[locationArray.length - 1]);
                        })
                        .catch(error => {
                            const response = error.response;
                            console.error('Playground', 'reserve', response ? response : error);
                            reject((response && response.data) ? response.data : null)
                        });
                })
                .catch(error => {
                    console.error('Playground', 'reserve', 'access error');
                    reject(null);
                });
        });
    }
}
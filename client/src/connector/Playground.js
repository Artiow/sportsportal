import ParamsSerializer from '../util/connector/ParamsSerializer';
import Authentication from './Authentication';
import API from '../boot/constants';
import axios from 'axios';

export default class Playground {

    static list(filter) {
        return new Promise((resolve, reject) => {
            axios
                .get(API.url('/playground/list'), {
                    paramsSerializer: ParamsSerializer.stringify(),
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
            axios.get(API.url(`/playground/${id}`))
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
                        .delete(API.url(`/playground/${id}`), {
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
            axios.get(API.url(`/playground/${id}/short`))
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
                .get(API.url(`/playground/${id}/board`), {
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
                .get(API.url(`/playground/${id}/check`), {
                    paramsSerializer: ParamsSerializer.stringify(),
                    params: {
                        reservations: reservations,
                        version: version
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
                        .post(API.url(`/playground/${id}/reserve`), {
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
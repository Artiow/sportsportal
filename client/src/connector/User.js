import Headers from '../util/connector/Headers';
import Authentication from './Authentication';
import API from '../boot/constants';
import axios from 'axios';

export default class User {

    static get(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    return axios.get(API.url(`/user/${id}`), Headers.bearer(token));
                })
                .then(response => {
                    console.debug('User', 'get', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('User', 'get', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                });
        });
    }

    static delete(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    return axios.delete(API.url(`/user/${id}`), Headers.bearer(token));
                })
                .then(response => {
                    console.debug('User', 'delete', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.error('User', 'delete', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }
}
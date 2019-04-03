import Headers from '../util/connector/Headers';
import Authentication from './Authentication';
import API from '../boot/constants';
import axios from 'axios';

export default class Order {

    static getList() {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    return axios.get(API.url(`/order/list`), Headers.bearer(token));
                })
                .then(response => {
                    console.debug('Order', 'list', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Order', 'list', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                });
        });
    }

    static get(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    return axios.get(API.url(`/order/${id}`), Headers.bearer(token));
                })
                .then(response => {
                    console.debug('Order', 'get', response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Order', 'get', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                })
        });
    }

    static delete(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    return axios.delete(API.url(`/order/${id}`), Headers.bearer(token));
                })
                .then(response => {
                    console.debug('Order', 'delete', response);
                    resolve();
                })
                .catch(error => {
                    const response = error.response;
                    console.error('Order', 'delete', response ? response : error);
                    reject((response && response.data) ? response.data : error);
                });
        });
    }
}
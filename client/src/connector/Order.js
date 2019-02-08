import Authentication from './Authentication';
import apiUrl from '../boot/constants';
import axios from 'axios';

export default class Order {

    static get(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    axios
                        .get(apiUrl(`/order/${id}`), {
                            headers: {Authorization: `Bearer ${token}`}
                        })
                        .then(response => {
                            console.debug('Order', 'get', response);
                            resolve(response.data);
                        })
                        .catch(error => {
                            const response = error.response;
                            console.error('Order', 'get', response ? response : error);
                            reject((response && response.data) ? response.data : null)
                        })
                })
                .catch(error => {
                    console.error('Order', 'get', 'access error');
                    reject(null);
                });
        });
    }

    static delete(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    axios
                        .delete(apiUrl(`/order/${id}`), {
                            headers: {Authorization: `Bearer ${token}`}
                        })
                        .then(response => {
                            console.debug('Order', 'delete', response);
                            resolve();
                        })
                        .catch(error => {
                            const response = error.response;
                            console.error('Order', 'delete', response ? response : error);
                            reject((response && response.data) ? response.data : null)
                        })
                })
                .catch(error => {
                    console.error('Order', 'delete', 'access error');
                    reject(null);
                });
        });
    }
}
import Authentication from './Authentication';
import apiUrl from '../boot/constants';
import axios from 'axios';

export default class User {

    static get(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    axios
                        .get(apiUrl(`/user/${id}`), {
                            headers: {'Authorization': `Bearer ${token}`}
                        })
                        .then(response => {
                            console.debug('User', 'get', response);
                            resolve(response.data);
                        })
                        .catch(error => {
                            const response = error.response;
                            console.error('User', 'get', response ? response : error);
                            reject((response && response.data) ? response.data : null)
                        })
                })
                .catch(error => {
                    console.error('User', 'get', 'access error');
                    reject(null);
                });
        });
    }

    static delete(id) {
        return new Promise((resolve, reject) => {
            Authentication.access()
                .then(token => {
                    axios
                        .delete(apiUrl(`/user/${id}`), {
                            headers: {'Authorization': `Bearer ${token}`}
                        })
                        .then(response => {
                            console.debug('User', 'delete', response);
                            resolve();
                        })
                        .catch(error => {
                            const response = error.response;
                            console.error('User', 'delete', response ? response : error);
                            reject((response && response.data) ? response.data : null)
                        })
                })
                .catch(error => {
                    console.error('User', 'delete', 'access error');
                    reject(null);
                });
        });
    }
}
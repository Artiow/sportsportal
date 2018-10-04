import {getApiUrl} from '../boot/constants'
import axios from 'axios';

/**
 * @param thenCallback {function(Object)}
 * @param catchCallback {function(Object)}
 */
export default function verify(thenCallback, catchCallback) {
    const accessToken = localStorage.getItem('token');
    if (accessToken !== null) {
        axios
            .get(getApiUrl('/auth/verify'), {params: {accessToken: accessToken}})
            .then(function (response) {
                console.log('Verify Response:', response);
                const data = response.data;
                localStorage.setItem('token', (data.tokenType + ' ' + data.tokenHash));
                localStorage.setItem('user', data.userInfo);
                if (typeof thenCallback === 'function') thenCallback(response);
            })
            .catch(function (error) {
                console.log('Verify Error:', ((error.response != null) ? error.response : error));
                localStorage.clear();
                if (typeof catchCallback === 'function') catchCallback(error);
            })
    }
}
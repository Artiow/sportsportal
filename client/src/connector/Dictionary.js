import apiUrl from '../boot/constants';
import axios from 'axios';

export default class Dictionary {

    static list(name) {
        return new Promise((resolve, reject) => {
            axios.get(apiUrl(`/dict/${name}/list`))
                .then(response => {
                    console.debug(`Dictionary [list:${name}]:`, response);
                    resolve(response.data);
                })
                .catch(error => {
                    const response = error.response;
                    console.error(`Dictionary [list:${name}]:`, response ? response : error);
                    reject((response && response.data) ? response.data : null)
                })
        });
    }
}
import Base64 from 'js-base64';

export default class Headers {

    static basic(username, password) {
        return {headers: {'Authorization': `Basic ${Base64.encode(username + ':' + password)}`}};
    }

    static bearer(token) {
        return {headers: {'Authorization': `Bearer ${token}`}};
    }
}
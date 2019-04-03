export default class Headers {

    static basic(username, password) {
        return {headers: {'Authorization': `Basic ${window.btoa(username + ':' + password)}`}};
    }

    static bearer(token) {
        return {headers: {'Authorization': `Bearer ${token}`}};
    }
}
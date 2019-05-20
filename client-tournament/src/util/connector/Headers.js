export default class Headers {

    static b64Encode(str) {
        return btoa(
            encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, (match, p1) => String.fromCharCode('0x' + p1))
        );
    }

    static basic(username, password) {
        return {headers: {'Authorization': `Basic ${Headers.b64Encode(username + ':' + password)}`}};
    }

    static bearer(token) {
        return {headers: {'Authorization': `Bearer ${token}`}};
    }
}
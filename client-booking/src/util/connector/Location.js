export default class Location {

    static extractId(headers) {
        const locationArray = headers.location.split('/');
        return locationArray[locationArray.length - 1];
    }
}
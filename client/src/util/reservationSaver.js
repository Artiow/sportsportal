/**
 * Reservation saver.
 * @param identifier {number} playground identifier
 * @param version {number} playground version
 * @param reservation {Array} reservation array
 */
export function saveReservation(identifier, version, reservation) {
    const content = {version: version, reservation: reservation};
    let map = getReservationMap();
    if (map == null) map = new Map([[identifier, content]]);
    else map.set(identifier, content);
    setReservationMap(map);
}

/**
 * Reservation restorer.
 * @param identifier {number} playground identifier
 * @param version {number} playground version
 * @return {Array} reservation array
 */
export function restoreReservation(identifier, version) {
    const map = getReservationMap();
    if (map == null) return [];
    const content = map.get(identifier);
    if (content == null) return [];
    const contentVersion = content.version;
    if ((contentVersion == null) || (contentVersion !== version)) return [];
    const contentReservation = content.reservation;
    if (contentReservation == null) return [];
    return contentReservation;
}

/**
 * Reservation restore cleaning.
 */
export function clearReservations() {
    localStorage.removeItem('reservations');
}

/**
 * @param map {Map<any, any>}
 */
function setReservationMap(map) {
    localStorage.setItem('reservations', mapToStr(map));
}

/**
 * @return {Map<any, any>}
 */
function getReservationMap() {
    return strToMap(localStorage.getItem('reservations'));
}

/**
 * @param map {Map<any, any>}
 * @return {String}
 */
function mapToStr(map) {
    if (map == null) return null;
    try {
        return JSON.stringify(Array.from(map.entries()));
    }
    catch (e) {
        return null;
    }
}

/**
 * @param str {String}
 * @return {Map<any, any>}
 */
function strToMap(str) {
    if (str == null) return null;
    try {
        return new Map(JSON.parse(str));
    }
    catch (e) {
        return null;
    }
}
export const connection = Object.freeze({
    API_URL: 'http://localhost:8080',
});

/**
 * Returns depending on {@link API_URL} URL API.
 * @param path {string}
 * @return {string}
 */
export function getApiUrl(path) {
    return connection.API_URL + path;
}
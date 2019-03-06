// noinspection JSUnresolvedVariable
export const env = Object.freeze({
    ROLE: Object.freeze({ADMIN: 'admin', USER: 'user'}),
    MAIN_HOST_URL: process.env.REACT_APP_MAIN_HOST,
    API_HOST_URL: process.env.REACT_APP_API_HOST,
    ANIMATION_TIMEOUT: 325,
    MONTH_NAMES: [
        'январь',
        'февраль',
        'март',
        'апрель',
        'май',
        'июнь',
        'июль',
        'август',
        'сентябрь',
        'октябрь',
        'ноябрь',
        'декабрь'
    ],
    DAYS_OF_WEEK_NAMES: [{
        full: 'воскресенье',
        short: 'вс'
    }, {
        full: 'понедельник',
        short: 'пн'
    }, {
        full: 'вторник',
        short: 'вт'
    }, {
        full: 'среда',
        short: 'ср'
    }, {
        full: 'четверг',
        short: 'чт'
    }, {
        full: 'пятница',
        short: 'пт'
    }, {
        full: 'суббота',
        short: 'сб'
    }]
});

/**
 * Returns depending on {@link API_HOST_URL} URL API.
 * @param path {string}
 * @return {string}
 */
export default function apiUrl(path) {
    return env.API_HOST_URL + path;
}
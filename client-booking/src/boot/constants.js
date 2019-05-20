// noinspection JSUnresolvedVariable
export const env = Object.freeze({
    ROLE: Object.freeze({ADMIN: 'admin', USER: 'user'}),
    YANDEX_MAP_API_KEY: process.env.REACT_APP_YANDEX_MAP_API_KEY,
    YANDEX_MAP_API_VERSION: process.env.REACT_APP_YANDEX_MAP_API_VERSION,
    MAIN_HOST: process.env.REACT_APP_MAIN_HOST,
    API_HOST: process.env.REACT_APP_API_HOST,
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

export default class API {

    /**
     * Returns depending on {@link API_HOST} URL API.
     * @param path {string}
     * @return {string}
     */
    static url(path) {
        return env.API_HOST + path;
    }
}
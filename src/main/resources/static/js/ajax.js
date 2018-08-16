/**
 * Default redirect in case of fail verification.
 * @type {string}
 */
const DEFAULT_FAIL_REDIRECT = '/login';

/**
 * Default redirect in case of successful logging.
 * @type {string}
 */
const DEFAULT_SUCCESSFUL_REDIRECT = '/home';

/**
 * Logout user.
 */
function ajaxLogout() {
    localStorage.clear();
    window.location.replace(DEFAULT_FAIL_REDIRECT);
}

/**
 * Logged user and redirects to a particular address.
 * @param login{string} - user login
 * @param password{string} - user password
 * @param errorHandler{function(number)} - error handler
 */
function ajaxLogin(login, password, errorHandler) {
    event.preventDefault();

    let redirect = localStorage.getItem('redirect');
    if (redirect === null) {
        redirect = DEFAULT_SUCCESSFUL_REDIRECT;
    }

    $.ajax({
        type: 'GET',
        url: '/api/user/login',
        data: {login: login, password: password},
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            const token = data.tokenType + ' ' + data.accessToken;
            const userURI = data.user.userURI;

            console.log('Accepted Token:', token);
            localStorage.setItem('token', token);
            localStorage.setItem('userURI', userURI);
            localStorage.removeItem('redirect');
            window.location.replace(redirect);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            errorHandler(jqXHR.status);
        }
    })
}

/**
 * Logging and getting user data by storied token and userURI.
 *
 * @param {string} securedURL - requested secured URL
 * @param {function(Object)} successEvent - function that is called in case of successful verification.
 */
function ajaxVerify(securedURL, successEvent) {
    const token = localStorage.getItem('token');
    if ((token === null) || (securedURL === null)) {
        localStorage.setItem('redirect', window.location.href);
        window.location.replace(DEFAULT_FAIL_REDIRECT);
    } else {
        $.ajax({
            type: 'GET',
            url: securedURL,
            dataType: 'json',
            beforeSend: function (jqXHR, settings) {
                console.log('Setting the Authorization Header:', token);
                jqXHR.setRequestHeader('Authorization', token);
            },
            success: function (data, textStatus, jqXHR) {
                console.log('Accepted Data:', data);
                successEvent(data)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Unexpected Error:', jqXHR.statusCode);
            }
        })
    }
}

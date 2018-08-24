const token = localStorage.getItem('token');
const userURL = localStorage.getItem('userURL');
if ((token !== null) && (userURL !== null)) {
    window.location.replace(DEFAULT_SUCCESSFUL_REDIRECT);
}

$(function () {
    $('#login-form').on("submit", function () {
        event.preventDefault();
        const login = $('#login').val();
        const password = $('#password').val();
        ajaxLogin(login, password, function (response) {
            $('#message').empty().append(response.message);
        });
    })
});
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
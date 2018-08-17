$(function () {
    $('#login').on("submit", function () {
        event.preventDefault();

        const login = $('#inputLogin').val();
        const password = $('#inputPassword').val();
        ajaxLogin(login, password, function (response) {
            $('#message').empty().append(response.message);
        });
    })
});
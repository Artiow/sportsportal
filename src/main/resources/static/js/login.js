$(function () {
    $('#login').on("submit", function () {
        const login = $('#inputLogin').val();
        const password = $('#inputPassword').val();
        ajaxLogin(login, password, function (statusCode) {
            const element = $('#message').empty();

            switch (statusCode) {
                case 404:
                    console.log('Error:', statusCode);
                    element.append('Неверный логин и/или пароль!');
                    break;
                default:
                    console.log('Unexpected error:', statusCode);
                    element.append('Неизвестная ошибка!');
                    break;
            }
        });
    })
});
$(function () {
    $('#logout').on("click", ajaxLogout);
});

const userURI = localStorage.getItem('userURI');

ajaxVerify(userURI, function (data) {
    $('#login').empty()
        .append(data.login);
    $('#full-name').empty()
        .append(data.name).append(' ')
        .append(data.surname).append(' ')
        .append(data.patronymic);
    $('body').fadeIn(0);
}, function (error) {
    $('#login')
        .empty()
        .remove();
    $('#full-name').empty()
        .append('Ошибка: ')
        .append(error.message);
    $('body').fadeIn(0);
});
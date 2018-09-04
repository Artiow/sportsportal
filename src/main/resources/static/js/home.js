$(function () {
    $('body').hide();
    $('#logout').on("click", ajaxLogout);
    ajaxVerify(localStorage.getItem('userURL'), function (data) {
        $('#login').empty()
            .append(data.login);
        $('#full-name').empty()
            .append(data.name).append(' ')
            .append(data.surname).append(' ')
            .append(data.patronymic);
        $('body').show();
    }, function (error) {
        $('#login')
            .empty()
            .remove();
        $('#full-name').empty()
            .append('Ошибка: ')
            .append(error.message);
        $('body').show();
    });
});
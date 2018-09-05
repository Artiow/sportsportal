$(function () {
    $('body').hide();
    $('#logout').on("click", ajaxLogout);
    ajaxVerify(localStorage.getItem('userURL'), function (data) {
        $('#nickname').empty()
            .append(data.login);
        $('#fullname').empty()
            .append(data.name).append(' ')
            .append(data.surname).append(' ')
            .append(data.patronymic);
        $('body').show();
    }, function (error) {
        $('#nickname').empty()
            .remove();
        $('#fullname').empty()
            .append('Ошибка: ')
            .append(error.message);
        $('body').show();
    });
});
$(function () {
    $('body').hide();
    ajaxAuth(function (data) {
        setMainHeader(data.login);
        $('#name').empty()
            .append(data.name).append(' ')
            .append(data.surname).append(' ')
            .append(data.patronymic);
        $('body').show();
    }, function (error) {
        localStorage.setItem('redirect', window.location.href);
        window.location.replace(DEFAULT_FAIL_REDIRECT);
    });
});
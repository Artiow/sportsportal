$(function () {
    $('body').hide();
    ajaxAuth(function (data) {
        setMainHeader(data.login);
        $('#name').empty()
            .append(data.name).append(' ')
            .append(data.surname).append(' ')
            .append(data.patronymic);
        $('body').show();
    }, function () {
        defaultFailedAuthorizationRedirect();
    }, function (error) {
        defaultFailedAuthorizationRedirect();
    });
});
$(function () {
    const header = $('header.header-main').hide();
    const container = $('main.container.container-home').hide();
    ajaxAuth(function (data) {
        setMainHeader(data.login);
        header.show();
        $('#name').empty()
            .append(data.name).append(' ')
            .append(data.surname).append(' ')
            .append(data.patronymic);
        container.show();
    }, function () {
        defaultFailedAuthorizationRedirect();
    }, function (error) {
        defaultFailedAuthorizationRedirect();
    });
});
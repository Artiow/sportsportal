$(function () {
    $('body').hide();
    ajaxAuth(function (data) {
        setMainHeader(data.login);
        $('body').show();
    }, function (error) {
        setMainHeader();
        $('body').show();
    });
});
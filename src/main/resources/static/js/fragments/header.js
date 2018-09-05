/**
 * Render main header.
 * @param {string} [login] - user login or nickname
 */
function setMainHeader(login) {
    const failedBlock = $('#auth-failed').hide();
    const succeedBlock = $('#auth-succeed').hide();
    if (login) {
        login = login.charAt(0).toUpperCase() + login.slice(1);
        $('#header-nickname').text(login);
        $('#header-logout').on("click", ajaxLogout);
        succeedBlock.show();
    } else {
        failedBlock.show();
    }
}
const NO_IMAGE_URL = "/img/no-image-sm.jpg";

const NOT_FOUND_CARD_LIST_COMPONENT = "";

/**
 * @param {number} rate
 * @return {string} html
 */
function compRate(rate) {
    let component = "<small class=\"card-rate\">\n";
    let i = 0;
    while (i <= (rate - 2)) {
        component += "<i class=\"fa fa-star\"></i>\n";
        i += 2;
    }
    if (i < rate) {
        component += "<i class=\"fa fa-star-half-o\"></i>\n";
        i += 2;
    }
    while (i < 10) {
        component += "<i class=\"fa fa-star-o\"></i>\n";
        i += 2;
    }
    return (component + "</small>\n");
}

/**
 * @param {object} playground
 * @return {string} html
 */
function compCard(playground) {
    const photoURLs = playground.photoURLs;
    const photoURL = (photoURLs.length > 0) ? (photoURLs[0] + "?size=sm") : NO_IMAGE_URL;
    return "<div class=\"col-xs-12 col-sm-6 mb-4\">\n" +
        "<div class=\"card\">\n" +
        "<img class=\"card-img\" src=\"" + photoURL + "\">\n" +
        "<div class=\"card-body\">\n" +
        "<h4 class=\"card-title\">\n" +
        "<small>" + playground.name + "</small>\n" +
        "</h4>\n" +
        "<h6 class=\"card-title\">\n" + compRate(playground.rate) + "</h6>\n" +
        "<p class=\"card-text\">\n" +
        "<span class=\"badge badge-dark\">от " + ((playground.cost / 100).toFixed()) + " <i class=\"fa fa-rub\"></i>/час</span>\n" +
        "</p>\n" +
        "<a href=\"/lease/playground?id=" + playground.id + "\" class=\"btn btn-outline-info btn-sm\">Подробнее...</a>\n" +
        "</div>\n" +
        "</div>\n" +
        "</div>\n";
}

/**
 * @param content {object[]}
 */
function compCardList(content) {
    let result;
    if (content.length > 0) {
        content.forEach(function (item, i, arr) {
            result += compCard(item);
        });
    } else {
        container.append(NOT_FOUND_CARD_LIST_COMPONENT);
    }
}

$(function () {
    $('body').hide();
    ajaxAuth(function (data) {
        setMainHeader(data.login);
        $('body').show();
    }, function () {
        setMainHeader();
        $('body').show();
    }, function (error) {
        setMainHeader();
        $('body').show();
    });
});
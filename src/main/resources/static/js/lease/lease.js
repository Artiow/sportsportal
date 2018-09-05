const NO_IMAGE_URL = "/img/no-image-sm.jpg";

const NOT_FOUND_CARD_LIST_COMPONENT =
    "<div class=\"col-xs-12 col-sm-12 mb-12\">\n" +
    "<div class=\"alert alert-primary\">\n" +
    "Ничего не найдено!\n" +
    "</div>\n" +
    "</div>\n";

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
 * @param {object[]} content
 * @return {string} html
 */
function compCardPanel(content) {
    let result = "";
    if (content.length > 0) {
        content.forEach(function (item, i, arr) {
            result += compCard(item);
        });
    } else {
        result += NOT_FOUND_CARD_LIST_COMPONENT;
    }
    return result;
}

/**
 * @param {object[]} content
 */
function renderCardPanel(content) {
    $('#container-lease-panel').html(compCardPanel(content));
}

/**
 * @param {number} number
 * @param {number} total
 */
function renderPagination(number, total) {

}

/**
 * @param divider {object}
 * @param successEvent {function(object)}
 * @param errorHandler {function(object)}
 */
function loadPage(divider, successEvent, errorHandler) {
    $.ajax({
        type: 'GET',
        url: '/api/lease/playground/list',
        contentType: 'application/json;charset=UTF-8',
        data: {pageSize: divider.pageSize, pageNum: divider.pageNum},
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            console.log('Accepted Data:', data);
            successEvent(data)
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('Accepted Error Data:', jqXHR.responseJSON);
            errorHandler(jqXHR.responseJSON);
        }
    });
}

const DEFAULT_PAGE_SIZE = 10;

let currentPage = 0;

/**
 * @param {number} index
 * @param {function()} callbackfn
 */
function renderPage(index, callbackfn) {
    currentPage = index;

    const queryJson = {
        pageSize: DEFAULT_PAGE_SIZE,
        pageNum: index
    };

    loadPage(queryJson, function (response) {
        renderCardPanel(response.content);
        renderPagination(response.pageNumber, response.totalPages);
        callbackfn();
    }, function (response) {

    });
}

$(function () {
    const header = $('header.header-main').hide();
    ajaxAuth(function (data) {
        setMainHeader(data.login);
        header.show();
    }, function () {
        setMainHeader();
        header.show();
    }, function (error) {
        setMainHeader();
        header.show();
    });

    const container = $('main.container.container-lease').hide();
    renderPage(0, function () {
        container.show();
    });
});
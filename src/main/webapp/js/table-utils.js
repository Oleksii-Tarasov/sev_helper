function applyRowColors() {
    $('.table tr:not(:first-child)').each(function () {
        const isTerminated = $(this).find('td:nth-last-child(2)').text().trim();
        const isConnected = $(this).find('td:last-child').text().trim();
        if (isConnected === 'Так') {
            $(this).addClass('connected-row');
        }
        if (isTerminated !== 'Ні') {
            $(this).addClass('terminated-row');
        }
    });
}

$(document).ready(function () {
    applyRowColors();
});

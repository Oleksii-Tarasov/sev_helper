$(document).ready(function() {
    const ws = new WebSocket('ws://' + window.location.host + contextPath + '/websocket/dbupdate');

    ws.onmessage = function (event) {
        if (event.data === 'reload') {
            console.log('Data update signal received');
            location.reload();
        }
    };

    ws.onclose = function () {
        // Trying to reconnect in 5 seconds
        setTimeout(function () {
            console.log('WebSocket disconnected. Trying to reconnect...');
            location.reload();
        }, 5000);
    };
});

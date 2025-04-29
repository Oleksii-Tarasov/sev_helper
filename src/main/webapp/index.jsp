<%@ page import="java.util.List" %>
<%@ page import="ua.gov.court.supreme.sevhelper.service.model.SevUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <%@include file="/css/page.css"%>
    </style>
    <title>Помічник СЕВ ОВВ</title>
</head>
<body>
<h1>Помічник СЕВ ОВВ</h1>
<form method="post" action="${pageContext.request.contextPath}/downloadUsers">
    <button type="submit">Download Users</button>
</form>

<!-- Error messages -->
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color: red;"><%= error %></p>
<%
    }
%>

<!-- Table output -->
<%
    List<SevUser> sevUsers = (List<SevUser>) request.getAttribute("sevUsers");
    if (sevUsers != null && !sevUsers.isEmpty()) {
%>
<div class="container-sm">
    <table class="table table-striped-columns">
        <tr>
            <th>№</th>
            <th>ЄДРПОУ</th>
            <th>Скорочене найменування</th>
            <th>Повне найменування</th>
            <th>Припинено</th>
            <th>Підключено до АС ДокПроф</th>
        </tr>
        <%
            int i = 1;
            for (SevUser user: sevUsers) {
        %>
            <tr>
                <td><%= i++ %></td>
                <td><%= user.getEdrpou() %></td>
                <td><%= user.getShortName() %></td>
                <td><%= user.getFullName() %></td>
                <td><%= user.getIsTerminated() %></td>
                <td><%= user.isConnected()? "Так" : "Ні" %></td>
            </tr>
        <% } %>
    </table>
</div>
<%
    }
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js" integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.min.js" integrity="sha384-VQqxDN0EQCkWoxt/0vsQvZswzTHUVOImccYmSyhJTp7kGtPed0Qcx8rK9h9YEgx+" crossorigin="anonymous"></script>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
    $(document).ready(function() {
        // Спочатку сортуємо таблицю за ЄДРПОУ
        sortTable(1);

        // Потім застосовуємо колірне оформлення
        $('.table tr:not(:first-child)').each(function() {
            const isTerminated = $(this).find('td:nth-last-child(2)').text().trim();
            const isConnected = $(this).find('td:last-child').text().trim();
            if (isConnected === 'Так') {
                $(this).addClass('connected-row');
            }
            if (isTerminated !== 'Ні') {
                $(this).addClass('terminated-row');
            }
        });
    });

    function sortTable(columnIndex) {
        const table = document.querySelector('.table');
        const tbody = table.querySelector('tbody') || table;
        const rows = Array.from(tbody.querySelectorAll('tr')).slice(1);

        const sortedRows = rows.sort((rowA, rowB) => {
            const cellA = rowA.cells[columnIndex].textContent;
            const cellB = rowB.cells[columnIndex].textContent;
            return cellA.localeCompare(cellB, 'uk', {numeric: true});
        });

        // Видаляємо існуючі рядки
        rows.forEach(row => row.parentNode.removeChild(row));

        // Додаємо відсортовані рядки
        sortedRows.forEach(row => tbody.appendChild(row));

        // Оновлюємо нумерацію
        let i = 1;
        sortedRows.forEach(row => {
            row.cells[0].textContent = i++;
        });
    }
</script>


<%--Зміна кольору рядка, якщо значення в стовпчиках "Так" або "Ні"--%>
<script>
    $(document).ready(function() {
        $('.table tr:not(:first-child)').each(function() {
            const isTerminated = $(this).find('td:nth-last-child(2)').text().trim();
            const isConnected = $(this).find('td:last-child').text().trim();
            if (isConnected === 'Так') {
                $(this).addClass('connected-row');
            }
            if (isTerminated !== 'Ні') {
                $(this).addClass('terminated-row');
            }
        });
    });
</script>
</body>
</html>

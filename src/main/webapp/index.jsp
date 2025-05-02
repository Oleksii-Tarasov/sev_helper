<%@ page import="java.util.List" %>
<%@ page import="ua.gov.court.supreme.sevhelper.model.SevUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">--%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <%@include file="/css/page.css"%>
    </style>
    <title>Довідник СЕВ ОВВ</title>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary py-0 fixed-top">
<%--    <div class="container-fluid">--%>
                <div class="container-sm">
                    <a class="navbar-brand" href="#">
            <img src="${pageContext.request.contextPath}/img/logo.jpg" alt="Logo" width="50" height="50"
                 class="d-inline-block align-text-middle">
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                       aria-expanded="false">
                        Довідник СЕВ ОВВ
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#">Довідка</a></li>
                        <li>
                            <form method="post" action="${pageContext.request.contextPath}/upd-users"
                                  style="margin: 0;">
                                <button type="submit" class="dropdown-item">Примусове оновлення</button>
                            </form>
                        </li>

                    </ul>
                </li>
            </ul>
        </div>


        <h6>Станом на: <%= request.getAttribute("lastUpdate") %></h6>
        <form class="d-flex" role="search">
            <input class="form-control me-2" type="search" placeholder="Назва організації/ЄДРПОУ" aria-label="Search">
            <button class="btn btn-outline-success" type="submit">Пошук</button>
        </form>
    </div>
</nav>

<!-- Error messages -->
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p style="color: red;"><%= error %>
</p>
<%
    }
%>

<!-- Table output -->
<%
    List<SevUser> sevUsers = (List<SevUser>) request.getAttribute("sevUsers");
    if (sevUsers != null && !sevUsers.isEmpty()) {
%>
<div class="container-sm mt-4">
    <div class="bg-white p-4 rounded shadow">
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
                for (SevUser user : sevUsers) {
            %>
            <tr>
                <td><%= i++ %>
                </td>
                <td><%= user.getEdrpou() %>
                </td>
                <td><%= user.getShortName() %>
                </td>
                <td><%= user.getFullName() %>
                </td>
                <td><%= user.getIsTerminated() %>
                </td>
                <td><%= user.isConnected() ? "Так" : "Ні" %>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
<%
    }
%>

<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<%--Зміна кольору рядка, якщо значення в стовпчиках "Так" або "Ні"--%>
<script>
    $(document).ready(function () {
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
    });
</script>
<script>
    // WebSocket з'єднання
    const ws = new WebSocket('ws://' + window.location.host + '${pageContext.request.contextPath}/websocket/dbupdate');

    ws.onmessage = function(event) {
        if (event.data === 'reload') {
            console.log('Отримано сигнал про оновлення даних');
            location.reload();
        }
    };

    ws.onclose = function() {
        // Спроба переконектитись через 5 секунд
        setTimeout(function() {
            console.log('WebSocket відєднався. Спроба переконектитись...');
            location.reload();
        }, 5000);
    };
</script>
</body>
</html>

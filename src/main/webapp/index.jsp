<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <%@include file="/css/style.css"%>
    </style>
    <title>Довідник СЕВ ОВВ</title>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary py-0 fixed-top">
    <div class="container-sm">
        <a class="navbar-brand" href="">
            <img src="${pageContext.request.contextPath}/img/logo.jpg" alt="Logo" width="50" height="50"
                 class="d-inline-block align-text-middle" onclick="location.reload(); return false;">
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
                        <li><a class="dropdown-item" href="" data-bs-toggle="modal"
                               data-bs-target="#helpModal">Довідка</a></li>
                        <li>
                            <form action="${pageContext.request.contextPath}/upload-file" method="post"
                                  enctype="multipart/form-data" style="margin: 0;">
                                <input type="file" id="file" name="file" accept=".xlsx" style="display: none;"
                                       onchange="this.form.submit();">
                                <button type="button" class="dropdown-item"
                                        onclick="document.getElementById('file').click();">
                                    Оновити дані з файлу
                                </button>
                            </form>
                        </li>
                        <li>
                            <form method="post" action="${pageContext.request.contextPath}/upd-users"
                                  style="margin: 0;">
                                <button type="submit" class="dropdown-item">Оновити дані з порталу "Дія"</button>
                            </form>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <h6>Станом на: <c:out value="${lastUpdate}"/></h6>
        <form class="d-flex" role="search" onsubmit="return false;">
            <input class="form-control me-2" type="search" id="searchInput" placeholder="Назва організації/ЄДРПОУ"
                   aria-label="Search">
            <button class="btn btn-outline-success" type="submit" onclick="globalHandleSearchClick()">Пошук</button>
        </form>
    </div>
</nav>

<!-- Error messages -->
<c:if test="${not empty error}">
    <p style="color: red;"><c:out value="${error}"/></p>
</c:if>

<!-- Table output -->
<c:if test="${not empty sevUsers}">
    <div class="container-sm mt-4">
        <div class="bg-white p-4 rounded shadow">
            <table class="table table-striped-columns">
                <tr>
                    <th>№</th>
                    <th data-sortable="true">ЄДРПОУ</th>
                    <th data-sortable="true">Скорочене найменування</th>
                    <th data-sortable="true">Повне найменування</th>
                    <th data-sortable="true">Припинено</th>
                    <th data-sortable="true">Підключено до АС ДокПроф</th>
                </tr>
                <c:forEach var="user" items="${sevUsers}" varStatus="status">
                    <tr>
                        <td><c:out value="${status.count}"/></td>
                        <td><c:out value="${user.edrpou}"/></td>
                        <td><c:out value="${user.shortName}"/></td>
                        <td><c:out value="${user.fullName}"/></td>
                        <td><c:out value="${user.isTerminated}"/></td>
                        <td><c:out value="${user.connected ? 'Так' : 'Ні'}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</c:if>

<!-- Modal help window -->
<div class="modal fade" id="helpModal" tabindex="-1" aria-labelledby="helpModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header justify-content-end">
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <h5 class="fw-bold mb-4">Опції «Довідника СЕВ ОВВ»:</h5>
                <ul class="list-unstyled">
                    <li class="mb-3">• Щоденне автоматизоване оновлення з порталу Дія (о 07:15 та 13:15) списку
                        учасників СЕВ ОВВ та перевірка їх статусу підключення
                    </li>
                    <li class="mb-4">• Примусове оновлення списку учасників СЕВ ОВВ з порталу Дія (не частіше ніж раз на
                        30 хвилин)
                    </li>
                </ul>

                <h5 class="fw-bold mb-3">Позначення кольорів:</h5>
                <ul class="list-unstyled">
                    <li class="mb-3 p-2" style="background-color: #90EE90">
                        організація налаштована до взаємодії через АС ДокПроф
                    </li>
                    <li class="mb-3 p-2" style="background-color: #FFB6C1">
                        участь організації в СЕВ ОВВ на даний момент припинена
                    </li>
                    <li class="mb-3 p-2" style="background-color: #FFFF00">
                        збіги пошукових запитів із записами в таблиці
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script>
    // Context path for JS
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/table-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/websocket.js"></script>
<script src="${pageContext.request.contextPath}/js/search.js"></script>
<script src="${pageContext.request.contextPath}/js/sort.js"></script>
</body>
</html>

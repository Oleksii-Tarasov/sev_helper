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
        <h6>Станом на: <%= request.getAttribute("lastUpdate") %>
        </h6>
        <form class="d-flex" role="search" onsubmit="return false;">
            <input class="form-control me-2" type="search" id="searchInput" placeholder="Назва організації/ЄДРПОУ"
                   aria-label="Search">
            <button class="btn btn-outline-success" type="submit" onclick="performSearch()">Пошук</button>
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
                <th data-sortable="true">ЄДРПОУ</th>
                <th data-sortable="true">Скорочене найменування</th>
                <th data-sortable="true">Повне найменування</th>
                <th data-sortable="true">Припинено</th>
                <th data-sortable="true">Підключено до АС ДокПроф</th>
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
<%--WebSocket з'єднання--%>
<script>
    const ws = new WebSocket('ws://' + window.location.host + '${pageContext.request.contextPath}/websocket/dbupdate');

    ws.onmessage = function (event) {
        if (event.data === 'reload') {
            console.log('Отримано сигнал про оновлення даних');
            location.reload();
        }
    };

    ws.onclose = function () {
        // Спроба переконектитись через 5 секунд
        setTimeout(function () {
            console.log('WebSocket відєднався. Спроба переконектитись...');
            location.reload();
        }, 5000);
    };
</script>
<%--Пошук--%>
<script>
    $(document).ready(function () {
        const searchInput = $('#searchInput');
        let currentMatchIndex = -1;
        let matchedCells = [];

        // Зберігаємо початковий стан рядків
        const rows = $('.table tr:not(:first-child)');
        const originalStates = [];

        rows.each(function () {
            originalStates.push({
                element: $(this),
                html: $(this).html(),
                classes: $(this).attr('class') || ''
            });
        });

        function restoreOriginalState() {
            originalStates.forEach(state => {
                state.element.html(state.html);
                state.element.attr('class', state.classes);
            });

            applyRowColors();
            matchedCells = [];
            currentMatchIndex = -1;
        }

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

        function scrollToMatch(index) {
            if (matchedCells.length === 0) return;

            // Забезпечуємо циклічність
            if (index >= matchedCells.length) {
                index = 0;
            } else if (index < 0) {
                index = matchedCells.length - 1;
            }

            currentMatchIndex = index;

            // Видаляємо попередній активний клас з усіх комірок
            $('.cell-match-active').removeClass('cell-match-active');

            // Додаємо активний клас поточній комірці
            const currentCell = matchedCells[currentMatchIndex];
            currentCell.addClass('cell-match-active');

            // Прокручуємо до поточного співпадіння
            $('html, body').animate({
                scrollTop: currentCell.offset().top - 100
            }, 500);
        }

        function performSearch() {
            const searchText = searchInput.val().trim().toLowerCase();

            if (!searchText) {
                restoreOriginalState();
                return;
            }

            restoreOriginalState();
            matchedCells = [];

            rows.each(function () {
                const row = $(this);
                const cells = row.find('td');

                cells.each(function () {
                    const cell = $(this);
                    const text = cell.text().toLowerCase();

                    if (text.includes(searchText)) {
                        cell.addClass('cell-match');
                        matchedCells.push(cell);
                    }
                });
            });

            // Показуємо кількість знайдених співпадінь
            if (matchedCells.length > 0) {
                scrollToMatch(0);
            }
        }

        function handleEnterKey(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                if (matchedCells.length > 0) {
                    scrollToMatch(currentMatchIndex + 1);
                } else {
                    performSearch();
                }
            }
        }

        searchInput.on('input', debounce(performSearch, 300));
        searchInput.on('keydown', handleEnterKey);

        function debounce(func, wait) {
            let timeout;
            return function () {
                const context = this;
                const args = arguments;
                clearTimeout(timeout);
                timeout = setTimeout(() => {
                    func.apply(context, args);
                }, wait);
            };
        }
    });
</script>
<%--Сортування--%>
<script>
    $(document).ready(function() {
        // Додаємо курсор-поінтер до заголовків колонок, що сортуються
        $('th[data-sortable="true"]').css('cursor', 'pointer');

        // Обробка кліку по заголовку
        $('th[data-sortable="true"]').click(function() {
            const table = $(this).parents('table').eq(0);
            const rows = table.find('tr:gt(0)').toArray();
            const column = $(this).index();
            const ascending = $(this).hasClass('asc');

            // Прибираємо клас сортування з інших колонок
            $(this).siblings('th').removeClass('asc desc');
            $(this).toggleClass('asc desc');

            // Сортування рядків
            rows.sort(function(a, b) {
                const A = $(a).find('td').eq(column).text().trim();
                const B = $(b).find('td').eq(column).text().trim();

                // Спеціальна логіка для колонок з "Так"/"Ні"
                if (A === 'Так' && B === 'Ні') return ascending ? 1 : -1;
                if (A === 'Ні' && B === 'Так') return ascending ? -1 : 1;

                // Перевірка на число (для ЄДРПОУ)
                if (!isNaN(A) && !isNaN(B)) {
                    return ascending ?
                        parseInt(A) - parseInt(B) :
                        parseInt(B) - parseInt(A);
                }

                // Звичайне текстове порівняння
                return ascending ?
                    A.localeCompare(B, 'uk') :
                    B.localeCompare(A, 'uk');
            });

            // Видаляємо всі рядки крім заголовка і додаємо відсортовані
            table.find('tr:gt(0)').remove();
            table.append(rows);

            // Оновлюємо нумерацію в першій колонці
            table.find('tr:gt(0)').each(function(index) {
                $(this).find('td:first').text(index + 1);
            });

            // Відновлюємо кольори рядків
            applyRowColors();
        });
    });
</script>
</body>
</html>

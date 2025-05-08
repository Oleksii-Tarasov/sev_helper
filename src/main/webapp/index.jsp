<%@ page import="java.util.List" %>
<%@ page import="ua.gov.court.supreme.sevhelper.model.SevUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <%@include file="/css/page.css"%>
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
                        <li><a class="dropdown-item" href="" data-bs-toggle="modal" data-bs-target="#helpModal">Довідка</a></li>
                        <li>
                            <form action=""
                                  style="margin: 0;">
                                <button type="submit" class="dropdown-item">Оновити дані з файлу</button>
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
        <h6>Станом на: <%= request.getAttribute("lastUpdate") %>
        </h6>
        <form class="d-flex" role="search" onsubmit="return false;">
            <input class="form-control me-2" type="search" id="searchInput" placeholder="Назва організації/ЄДРПОУ"
                   aria-label="Search">
            <button class="btn btn-outline-success" type="submit" onclick="globalHandleSearchClick()">Пошук</button>
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
                    <li class="mb-3">• Щоденне автоматизоване оновлення з порталу Дія (о 07:15 та 13:15) списку учасників СЕВ ОВВ та перевірка їх статусу підключення</li>
                    <li class="mb-4">• Ручне оновлення списку учасників СЕВ ОВВ за потреби, але не частіше ніж раз на 30 хвилин</li>
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

<script src="${pageContext.request.contextPath}/css/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/css/bootstrap.bundle.min.js"></script>
<%--Change row color if values in columns are "Yes" or "No"--%>
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
<%--WebSocket connection--%>
<script>
    const ws = new WebSocket('ws://' + window.location.host + '${pageContext.request.contextPath}/websocket/dbupdate');

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
</script>
<%--Search--%>
<script>
    let globalHandleSearchClick;

    $(document).ready(function () {
        const searchInput = $('#searchInput');
        let currentMatchIndex = -1;
        let matchedCells = [];

        // Preserve the initial state of the rows
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

            // Ensure cyclical search
            if (index >= matchedCells.length) {
                index = 0;
            } else if (index < 0) {
                index = matchedCells.length - 1;
            }

            currentMatchIndex = index;

            // Remove the previous active class from all cells
            $('.cell-match-active').removeClass('cell-match-active');

            // Add an active class to the current cell
            const currentCell = matchedCells[currentMatchIndex];
            currentCell.addClass('cell-match-active');

            // Scroll to the current match
            $('html, body').animate({
                scrollTop: currentCell.offset().top - 300
            }, 250);
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

            // Show the number of matches found
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

        // Search Button
        globalHandleSearchClick = function() {
            if (matchedCells.length > 0) {
                scrollToMatch(currentMatchIndex + 1);
            } else {
                performSearch();
            }
        }

        //Autoscroll to top if the search input is empty
        searchInput.on('search', function() {
            if (this.value === '') {
                $('html, body').animate({ scrollTop: 0 }, 250);
            }
        });
    });
</script>
<%--Sort--%>
<script>
    $(document).ready(function () {
        // Add a cursor pointer to the column headings being sorted
        $('th[data-sortable="true"]').css('cursor', 'pointer');

        // Handling a click on the title
        $('th[data-sortable="true"]').click(function () {
            const table = $(this).parents('table').eq(0);
            const rows = table.find('tr:gt(0)').toArray();
            const column = $(this).index();
            const ascending = $(this).hasClass('asc');

            // Removing the sort class from other columns
            $(this).siblings('th').removeClass('asc desc');
            $(this).toggleClass('asc desc');

            // Sorting rows
            rows.sort(function (a, b) {
                const A = $(a).find('td').eq(column).text().trim();
                const B = $(b).find('td').eq(column).text().trim();

                // Special logic for "Yes"/"No" columns
                if (A === 'Так' && B === 'Ні') return ascending ? 1 : -1;
                if (A === 'Ні' && B === 'Так') return ascending ? -1 : 1;

                // Number check (for EDRPOU)
                if (!isNaN(A) && !isNaN(B)) {
                    return ascending ?
                        parseInt(A) - parseInt(B) :
                        parseInt(B) - parseInt(A);
                }

                // Plain text comparison
                return ascending ?
                    A.localeCompare(B, 'uk') :
                    B.localeCompare(A, 'uk');
            });

            // Delete all rows except the header and add sorted ones
            table.find('tr:gt(0)').remove();
            table.append(rows);

            // Update the numbering in the first column
            table.find('tr:gt(0)').each(function (index) {
                $(this).find('td:first').text(index + 1);
            });

            // Restoring row colors
            applyRowColors();
        });
    });
</script>
</body>
</html>

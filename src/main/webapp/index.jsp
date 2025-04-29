<%@ page import="java.util.List" %>
<%@ page import="ua.gov.court.supreme.sevhelper.service.model.SevUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-SgOJa3DmI69IUzQ2PVdRZhwQ+dy64/BUtbMJw1MZ8t5HZApcHrRKUc4W0kG879m7" crossorigin="anonymous">
    <title>sevhelper</title>
</head>
<body>
<h1>Sev Helper</h1>
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
<%
    }
%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js" integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.min.js" integrity="sha384-VQqxDN0EQCkWoxt/0vsQvZswzTHUVOImccYmSyhJTp7kGtPed0Qcx8rK9h9YEgx+" crossorigin="anonymous"></script>
</body>
</html>

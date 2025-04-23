<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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
    List<String[]> sevUsers = (List<String[]>) request.getAttribute("sevUsers");
    if (sevUsers != null && !sevUsers.isEmpty()) {
%>
    <table>
        <tr>
            <th>№</th>
            <th>ЄДРПОУ</th>
            <th>Скорочене найменування</th>
            <th>Повне найменування</th>
            <th>Припинено</th>
        </tr>
        <%
            int i = 1;
            for (String[] row: sevUsers) {
        %>
            <tr>
                <td><%= i++ %></td>
                <td><%= row[0] %></td>
                <td><%= row[1] %></td>
                <td><%= row[2] %></td>
                <td><%= row[3] %></td>
            </tr>
        <% } %>
    </table>
<%
    }
%>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="gameofmath.Html" %>
<%
    String ctx = request.getContextPath();
    Object error = request.getAttribute("error");
    Object info = request.getAttribute("info");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Log in - Game of Math</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body>
<main class="card">
    <h1>Game of Math</h1>
    <h2>Log in</h2>

    <% if (info != null) { %><p class="msg ok"><%= Html.escape(info) %></p><% } %>
    <% if (error != null) { %><p class="msg bad"><%= Html.escape(error) %></p><% } %>

    <form action="<%= ctx %>/login" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" autocomplete="username"
               value="<%= Html.escape(request.getAttribute("username")) %>" required maxlength="20">

        <label for="password">Password</label>
        <input type="password" id="password" name="password" autocomplete="current-password" required>

        <button type="submit">Log in</button>
    </form>

    <p class="alt">No account yet? <a href="<%= ctx %>/signup">Sign up</a></p>
</main>
</body>
</html>

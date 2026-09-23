<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="gameofmath.Html, gameofmath.UserStore" %>
<%
    String ctx = request.getContextPath();
    Object error = request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign up - Game of Math</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body>
<main class="card">
    <h1>Game of Math</h1>
    <h2>Create an account</h2>

    <% if (error != null) { %><p class="msg bad"><%= Html.escape(error) %></p><% } %>

    <form action="<%= ctx %>/signup" method="post">
        <label for="username">Username <small>(3-20 letters, digits or _)</small></label>
        <input type="text" id="username" name="username" autocomplete="username"
               value="<%= Html.escape(request.getAttribute("username")) %>"
               required minlength="3" maxlength="20" pattern="[A-Za-z0-9_]{3,20}">

        <label for="password">Password <small>(at least <%= UserStore.MIN_PASSWORD_LENGTH %> characters)</small></label>
        <input type="password" id="password" name="password" autocomplete="new-password"
               required minlength="<%= UserStore.MIN_PASSWORD_LENGTH %>" maxlength="<%= UserStore.MAX_PASSWORD_LENGTH %>">

        <label for="confirm">Confirm password</label>
        <input type="password" id="confirm" name="confirm" autocomplete="new-password" required>

        <button type="submit">Sign up</button>
    </form>

    <p class="alt">Already registered? <a href="<%= ctx %>/login">Log in</a></p>
</main>
</body>
</html>

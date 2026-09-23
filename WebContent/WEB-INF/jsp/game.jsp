<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="gameofmath.Html, gameofmath.GameState" %>
<%
    String ctx = request.getContextPath();
    GameState game = (GameState) request.getAttribute("game");
    Object feedback = request.getAttribute("feedback");
    boolean positive = Boolean.TRUE.equals(request.getAttribute("feedbackPositive"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Play - Game of Math</title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body>
<main class="card">
    <header class="top">
        <span>Player: <strong><%= Html.escape(request.getAttribute("user")) %></strong></span>
        <form action="<%= ctx %>/logout" method="post">
            <button type="submit" class="link">Log out</button>
        </form>
    </header>

    <h1>Game of Math</h1>

    <% if (feedback != null) { %>
        <p class="msg <%= positive ? "ok" : "bad" %>"><%= Html.escape(feedback) %></p>
    <% } %>

    <form action="<%= ctx %>/game" method="post" class="quiz">
        <label for="answer" class="problem"><%= Html.escape(game.getProblemText()) %> = ?</label>
        <input type="text" id="answer" name="answer" inputmode="numeric" pattern="-?[0-9]{1,7}"
               maxlength="7" autocomplete="off" required autofocus>
        <button type="submit">Check answer</button>
    </form>

    <dl class="stats">
        <div><dt>Correct</dt><dd><%= game.getCorrect() %> / <%= game.getAttempts() %></dd></div>
        <div><dt>Streak</dt><dd><%= game.getStreak() %></dd></div>
        <div><dt>Best streak</dt><dd><%= game.getBestStreak() %></dd></div>
    </dl>

    <form action="<%= ctx %>/game" method="post">
        <input type="hidden" name="action" value="reset">
        <button type="submit" class="link">Reset score</button>
    </form>
</main>
</body>
</html>

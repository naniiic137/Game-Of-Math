<%-- Entry point: the game page redirects to the login page when nobody is logged in. --%>
<% response.sendRedirect(request.getContextPath() + "/game"); %>

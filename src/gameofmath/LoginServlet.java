package gameofmath;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** GET shows the login form, POST checks the credentials against the {@link UserStore}. */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    static final String VIEW = "/WEB-INF/jsp/login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Auth.currentUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/game");
            return;
        }
        if (request.getParameter("loggedOut") != null) {
            request.setAttribute("info", "You have been logged out.");
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String user = UserStore.get(getServletContext()).authenticate(username, password);
        if (user == null) {
            request.setAttribute("error", "Invalid username or password.");
            request.setAttribute("username", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher(VIEW).forward(request, response);
            return;
        }
        Auth.logIn(request, user);
        response.sendRedirect(request.getContextPath() + "/game");
    }
}

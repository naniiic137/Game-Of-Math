package gameofmath;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** GET shows the sign-up form, POST creates the account and logs the new user in. */
@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/jsp/signup.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Auth.currentUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/game");
            return;
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        UserStore.Result result = UserStore.get(getServletContext()).register(
                username, request.getParameter("password"), request.getParameter("confirm"));

        if (!result.ok()) {
            request.setAttribute("error", result.error);
            request.setAttribute("username", username);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(VIEW).forward(request, response);
            return;
        }
        Auth.logIn(request, result.username);
        response.sendRedirect(request.getContextPath() + "/game");
    }
}

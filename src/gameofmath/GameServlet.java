package gameofmath;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The quiz. GET shows the current problem and score; POST checks an answer (or resets the
 * score) and redirects back to GET, so refreshing the page never re-submits an answer.
 */
@WebServlet("/game")
public class GameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/jsp/game.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GameState game = requireGame(request, response);
        if (game == null) {
            return;
        }
        request.setAttribute("user", Auth.currentUser(request));
        request.setAttribute("game", game);
        request.setAttribute("feedbackPositive", game.isFeedbackPositive());
        request.setAttribute("feedback", game.takeFeedback());
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GameState game = requireGame(request, response);
        if (game == null) {
            return;
        }
        if ("reset".equals(request.getParameter("action"))) {
            game.reset();
        } else {
            game.submit(request.getParameter("answer"));
        }
        response.sendRedirect(request.getContextPath() + "/game");
    }

    /** Returns the player's game, or redirects to the login page and returns null. */
    private static GameState requireGame(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (Auth.currentUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        HttpSession session = request.getSession();
        GameState game = (GameState) session.getAttribute(GameState.SESSION_KEY);
        if (game == null) {
            game = new GameState();
            session.setAttribute(GameState.SESSION_KEY, game);
        }
        return game;
    }
}

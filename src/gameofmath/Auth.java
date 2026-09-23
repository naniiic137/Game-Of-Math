package gameofmath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** Session helpers shared by the servlets. */
public final class Auth {

    public static final String USER_KEY = "user";

    private Auth() {
    }

    /** The logged-in username, or null if there is no authenticated session. */
    public static String currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (String) session.getAttribute(USER_KEY);
    }

    /**
     * Starts a fresh session for the user. The old session (if any) is discarded first
     * so a session id obtained before login cannot be reused afterwards (session fixation).
     */
    public static void logIn(HttpServletRequest request, String username) {
        HttpSession old = request.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_KEY, username);
        session.setAttribute(GameState.SESSION_KEY, new GameState());
    }
}

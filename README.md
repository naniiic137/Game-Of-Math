# Game of Math

A small Java web app built with plain **Servlets and JSP** and no framework. Players create an account, log in and answer random arithmetic questions while the app tracks their score and streak.

I built it as a student project to practise the servlet request/response cycle, HTTP sessions and server-side validation.

## Features

- **Sign up / log in / log out** with session-based authentication.
  - Usernames: 3–20 letters, digits or `_` (case-insensitive, so `Hamza` and `hamza` count as the same name).
  - Passwords: at least 6 characters, stored only as a **salted PBKDF2-HMAC-SHA256 hash** (random 16-byte salt per user, JDK built-ins only).
  - Starting a session after login throws away the old session ID, which protects against session fixation. Logout is a POST request.
- **Math quiz** with `+`, `−`, `×` and `÷`:
  - Every answer is a whole number. Subtraction never goes negative, multiplication uses the 0–12 times tables, and division is built from a multiplication, so the divisor is never zero and the result is always exact.
  - The current problem and its answer are kept in the **HTTP session**. The page never contains them, so viewing the source reveals nothing.
  - Input is validated. Anything that is not a whole number shows a friendly message and does not count as an attempt, so the app never throws `NumberFormatException` or `ArithmeticException`.
  - Tracks **correct / attempts**, **current streak** and **best streak** per session, with a reset button.
  - Uses Post/Redirect/Get, so refreshing the page never re-submits an answer.
- JSPs live under `WEB-INF/`, so the browser cannot open them directly. Every value printed in a page is HTML-escaped.

## Tech

Java 8+, Servlet 3.0 API (`javax.servlet`), JSP, HTML/CSS. Built as an Eclipse *Dynamic Web Project*.

## Running it

Requirements: a JDK (8 or newer) and **Apache Tomcat 7, 8.5 or 9**. The code uses `javax.servlet`; Tomcat 10+ switched to `jakarta.servlet` and will not run it without migration.

### Eclipse (Enterprise Java / Web Developers edition)

1. `git clone https://github.com/naniiic137/Game-Of-Math.git`
2. *File → Import → General → Existing Projects into Workspace* and select the cloned folder.
3. If Eclipse reports a missing target runtime, open *Project → Properties → Targeted Runtimes* and tick your Tomcat installation (add one under *Window → Preferences → Server → Runtime Environments* if needed).
4. Right-click the project, then *Run As → Run on Server* and choose Tomcat.
5. Open `http://localhost:8080/Game-Of-Math/`, create an account and play.

### Plain Tomcat (without Eclipse)

Compile the classes into `WebContent/WEB-INF/classes`, then copy `WebContent` into Tomcat's `webapps` folder as `Game-Of-Math`:

```bash
javac -cp "$CATALINA_HOME/lib/servlet-api.jar" -d WebContent/WEB-INF/classes src/gameofmath/*.java
cp -r WebContent "$CATALINA_HOME/webapps/Game-Of-Math"
```

## Project structure

```
src/gameofmath/
  LoginServlet.java    /login   - login form + credential check
  SignUpServlet.java   /signup  - registration form + validation
  LogoutServlet.java   /logout  - ends the session (POST)
  GameServlet.java     /game    - shows the problem, checks answers, resets score
  GameState.java       per-session problem + score/streak counters
  Problem.java         random integer-friendly problem generator
  UserStore.java       in-memory user store (ServletContext attribute)
  PasswordHasher.java  salted PBKDF2 hashing, constant-time comparison
  Auth.java            session helpers (current user, login with new session)
  Html.java            HTML escaping for JSP output
WebContent/
  index.jsp            redirects to /game (which redirects to /login if needed)
  style.css
  WEB-INF/web.xml      welcome file + session settings (HttpOnly cookie, 30 min)
  WEB-INF/jsp/         login.jsp, signup.jsp, game.jsp
```

## Limitations

- **Accounts live in memory** (`ServletContext`). Restarting or redeploying Tomcat erases every account. A real deployment would use a database.
- Scores are per session only. There is no persistent history or leaderboard.
- There is no CSRF token, rate limiting or account lockout, and HTTPS depends on how Tomcat is configured. It is fine for a local demo but not hardened for production.
- No automated test suite is included in the repository.

## License

© 2026 Hamza Ben Ismail. All rights reserved.

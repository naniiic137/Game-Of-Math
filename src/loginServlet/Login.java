package loginServlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String username = "admin";
    private final String password = "password";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext context = getServletContext();
        String storedUsername = (String) context.getAttribute("username");
        String storedPassword = (String) context.getAttribute("password");

        String enteredUsername = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
            // Authentication successful, create session
            request.getSession().setAttribute("username", enteredUsername);
            response.sendRedirect("welcome.jsp"); // Redirect to welcome page
        } else {
            // Authentication failed, show error message
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
		
		/*
		
		// TODO Auto-generated method stub
		String enteredUsername = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
            // Authentication successful, create session
            HttpSession session = request.getSession();
            session.setAttribute("username", enteredUsername);
            response.sendRedirect("welcome.jsp"); // Redirect to welcome page
        } else {
            // Authentication failed, show error message
            request.setAttribute("errorMessage", "Invalid username or password");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
	}
*/
}

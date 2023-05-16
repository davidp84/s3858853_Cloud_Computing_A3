package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.LoginController;
import model.LoggedInUser;
import model.User;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserLoginServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		LoginController login = new LoginController();

		try {
			User user = login.checkLogin(email, password);
			String destPage = "login.jsp";
			
			if (user != null) {
				String username = user.getUser_name();
				// Set user_name
				LoggedInUser temp = new LoggedInUser();
				temp.setUser_name(username);
				destPage = "main.jsp";
			} else {
				String message = "email or password is invalid";
				request.setAttribute("message", message);
			}

			RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
			dispatcher.forward(request, response);

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
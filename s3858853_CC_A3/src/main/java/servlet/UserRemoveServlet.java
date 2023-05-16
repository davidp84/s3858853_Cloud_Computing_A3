package servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.BreakdownController;

@WebServlet("/remove")
public class UserRemoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserRemoveServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String user = request.getParameter("removeUser");
		String event = request.getParameter("removeDate");

		BreakdownController bc = new BreakdownController();

		try {
			bc.removeEvent(user, event);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String destPage = "main.jsp";

		RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
		dispatcher.forward(request, response);

	}
}

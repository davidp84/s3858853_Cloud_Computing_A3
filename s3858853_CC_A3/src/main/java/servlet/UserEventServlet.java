package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.BreakdownController;
import model.BreakdownEvent;
import model.Event;

@WebServlet("/event")
public class UserEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserEventServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		String user = request.getParameter("user");
		String event = request.getParameter("event");

		// J. Paul, “How to convert string to localdatetime in java 8 - example
		// tutorial,” Java67, 24-Aug-2021.
		// [Online]. Available:
		// https://www.java67.com/2016/04/how-to-convert-string-to-localdatetime-in-java8-example.html.
		// [Accessed: 09-May-2022].
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime now = LocalDateTime.now();
		String formatDateTime = now.format(formatter);

		// Create breakdown event to pass to controller for databse upload.
		BreakdownEvent breakdown = new BreakdownEvent();
		breakdown.setCurrent(true);
		breakdown.setDateTimeUTC(formatDateTime);
		breakdown.setEvent(Event.valueOf(event.toUpperCase()));
		breakdown.setLatitude(latitude);
		breakdown.setLongitude(longitude);
		breakdown.setUserName(user);

		BreakdownController bc = new BreakdownController();

		try {
			bc.createEvent(breakdown);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String destPage = "main.jsp";

		RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
		dispatcher.forward(request, response);

	}

}

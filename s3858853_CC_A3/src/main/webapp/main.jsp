<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="model.Event"%>
<%@ page import="model.LoggedInUser"%>
<%@ page import="model.BreakdownEvent"%>
<%@ page import="controller.MainController"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Main Page</title>
<script src='script.js'></script>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<script async defer type="text/javascript">
	google.charts.load('current', {
		'packages' : [ 'corechart' ]
	});
	google.charts.setOnLoadCallback(drawChart);
</script>
<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>

</head>
<body>

	<div id="fb-root"></div>
	<script async defer crossorigin="anonymous"
		src="https://connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v13.0"
		nonce="REDACTED"></script>

	<h1 class="display-4">Breakdown Event</h1>
	<div></div>
	<div>
		<h2>Create a new event</h2>
	</div>

	<div>
		<form action="event" method="post">
			<label>Select Event Type </label> <br> <br> <input
				name="latitude" id="latitude" type="hidden"><input
				name="longitude" id="longitude" type="hidden"> <input
				name="user" value="<%=LoggedInUser.USER_NAME%>" type="hidden">
			<fieldset>
				<INPUT TYPE="radio" name="event" value="<%=Event.FLAT_BATTERY%>" />Flat
				Battery <INPUT TYPE="radio" name="event"
					value="<%=Event.FLAT_TYRE%>" />Flat Tyre <INPUT TYPE="radio"
					name="event" value="<%=Event.MECHANICAL%>" />Mechanical Issue <INPUT
					TYPE="radio" name="event" value="<%=Event.NO_PETROL%>" />No Petrol
				<INPUT TYPE="radio" name="event" value="<%=Event.UNKNOWN%>" />Not
				Sure
			</fieldset>
			<br> <label>Enter Address: </label> <input name="address"
				id="address" type="text" size="100px" required onchange="getPosition();"> <button type="button">Confirm Address</button> <br> <br> <input
				id="event" value="Create Event" type="submit"
				onclick="getPosition();">
		</form>
	</div>
	<div>
		<h2>Current Events</h2>
	</div>

	<!-- ramazan polat and Leroy Gumede,  -->
	<!-- "Map isn't showing on Google Maps JavaScript API v3 when nested in a div tag,"  -->
	<!-- Stack Overflow, 14-Apr-2015. [Online]. Available:  -->
	<!-- https://stackoverflow.com/questions/16349476/map-isnt-showing-on-google-maps-javascript-api-v3-when-nested-in-a-div-tag. -->
	<!--  [Accessed: 07-May-2022].  -->
	<div style="height: 100%; width: 100%;"></div>
	<div id="map" style="height: 600px; width: 99%;"></div>
	<script async
		src="https://maps.googleapis.com/maps/api/js?key=REDACTED=initMap"></script>

	<h2>Control Panel</h2>

	<div>
		<h3><%=LoggedInUser.USER_NAME%></h3>
		<%
		MainController main = new MainController();
		%>
		<div>
			<%
			List<BreakdownEvent> events = main.getCurrentEvents(LoggedInUser.USER_NAME);
			if (events == null) {
			%>
			<h3>No Current Events</h3>
			<%
			} else {
			%>
			<table class="main-table">
				<tr class="table-header">
					<th>Breakdown Event</th>
					<th>Time of event</th>
					<th>Latitude</th>
					<th>Longitude</th>
					<th>Action</th>
				</tr>
				<%
				for (BreakdownEvent event : events) {
				%>

				<tr class="table-row">
					<td><%=event.getEventString()%></td>
					<td><%=event.getDateTimeUTC()%></td>
					<td><%=event.getLatitude()%></td>
					<td><%=event.getLongitude()%></td>
					<td><form action="remove" method="post">
							<input name="removeDate" value="<%=event.getDateTimeUTC()%>"
								type="hidden"><input name="removeUser"
								value="<%=LoggedInUser.USER_NAME%>" type="hidden"> <input
								value="Remove" type="submit">
						</form></td>

					<%
					}
					%>
				</tr>
			</table>

			<%
			}
			%>
		</div>
	</div>
	<br>
	<div class="fb-share-button"
		data-href="http://s3858853a3-env-1.eba-awfmeiww.us-east-1.elasticbeanstalk.com/main.jsp"
		data-layout="button_count" data-size="large">
		<a target="_blank"
			href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fs3858853a3-env-1.eba-awfmeiww.us-east-1.elasticbeanstalk.com%2Fmain.jsp&amp;src=sdkpreparse"
			class="fb-xfbml-parse-ignore">Share</a>
	</div>
	<br>
	<br>

	<div id="piechart" style="width: 900px; height: 500px;"></div>

	<br>
	<br>

	<div class="row">
		<div class="col-md-4">
			<form action="login.jsp">
				<div class="form-group">
					<input type="submit" value="Log Out" name="Log Out"
						class="btn btn-primary" />
				</div>
			</form>
		</div>
	</div>
</body>
</html>
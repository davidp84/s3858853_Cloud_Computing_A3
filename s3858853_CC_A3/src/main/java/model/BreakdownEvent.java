package model;

import javax.persistence.Entity;

@Entity
public class BreakdownEvent {

	private Event event;
	private double latitude;
	private double longitude;
	// Status of the event. True if current, false if completed.
	private boolean current;
	private String dateTimeUTC;
	private String userName;

	public String getEventString() {
		return event.toString();
	}
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public String getDateTimeUTC() {
		return dateTimeUTC;
	}

	public void setDateTimeUTC(String dateTimeUTC) {
		this.dateTimeUTC = dateTimeUTC;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

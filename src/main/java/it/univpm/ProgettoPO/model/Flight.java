package it.univpm.ProgettoPO.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe per mappare un volo delle API esterne. Utilizza solo i dati fondamentali.
 */
public class Flight {
	private String number;
	private boolean cargo;
	private Aircraft aircraft;
	private String airline;
	private Date scheduledTime;
	private Date actualTime;
	private String category;
	
	/**
	 * Getters e Setters
	 */
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public boolean isCargo() {
		return cargo;
	}
	public void setCargo(boolean cargo) {
		this.cargo = cargo;
	}
	public Aircraft getAircraft() {
		return aircraft;
	}
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public Date getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public Date getActualTime() {
		return actualTime;
	}
	public void setActualTime(Date actualTime) {
		this.actualTime = actualTime;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	/**
	 * @param obj Oggetto JSON da convertire
	 * @return Un oggetto 'Flight'
	 * 
	 * @throws JSONException
	 */
	public static Flight fromJson(JSONObject obj, String category) throws JSONException {
		Flight flight = new Flight();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mmX");
		
		flight.setNumber(obj.getString("number"));
		flight.setCargo(obj.getBoolean("isCargo"));
		flight.setAircraft((obj.has("aircraft")) ? Aircraft.fromJson(obj.getJSONObject("aircraft")) : null);
		flight.setAirline(obj.getJSONObject("airline").getString("name"));

		flight.setScheduledTime(sdf.parse(obj.getJSONObject("movement").getString("scheduledTimeLocal"), new ParsePosition(0)));
		flight.setActualTime(sdf.parse(obj.getJSONObject("movement").getString("actualTimeLocal"), new ParsePosition(0)));

		flight.setCategory(category);
		
		return flight;
	}
}

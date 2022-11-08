package it.univpm.ProgettoPO.model;

import java.util.LinkedList;
import java.text.DecimalFormat;

public class AirlineStatistic {
	private String airline;
	private String cargoPercentage;
	private String arrivalPercentage;
	private String departurePercentage;
	
	
	// Costruttore
	protected AirlineStatistic(String airline, String cargoPercentage, String arrivalPercentage, String departurePercentage) {
		this.airline = airline;
		this.cargoPercentage = cargoPercentage;
		this.arrivalPercentage = arrivalPercentage;
		this.departurePercentage = departurePercentage;
	}
	
	
	// Getters e Setters
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getCargoPercentage() {
		return cargoPercentage;
	}
	public void setCargoPercentage(String cargoPercentage) {
		this.cargoPercentage = cargoPercentage;
	}
	public String getArrivalPercentage() {
		return arrivalPercentage;
	}
	public void setArrivalPercentage(String arrivalPercentage) {
		this.arrivalPercentage = arrivalPercentage;
	}
	public String getDeparturePercentage() {
		return departurePercentage;
	}
	public void setDeparturePercentage(String departurePercentage) {
		this.departurePercentage = departurePercentage;
	}



	/**
	 * Data una lista di voli ed un'airline, ne calcola le statistiche
	 * 
	 * @param source Lista di voli
	 * @param airline Airline per cui calcolare le statistiche
	 * 
	 * @return Statistiche relative all'airline data
	 */
	public static AirlineStatistic getFromFlightList(LinkedList<Flight> source, String airline) {
		LinkedList<Flight> filtered = new LinkedList<Flight>();
		
		int cargos = 0;
		int arrivals = 0;
		
		for(Flight f: source) {
			if (f.getAirline().equals(airline))
				filtered.add(f);
		}
		
		for(Flight f: filtered) {
			if (f.isCargo())
				cargos++;
			if (f.getCategory().toLowerCase().equals("arrival"))
				arrivals++;
		}
		
		DecimalFormat df = new DecimalFormat("#%");
		double cargoPercentage = ((double) cargos / (double) filtered.size());
		double arrivalPercentage = ((double) arrivals / (double) filtered.size());
		double departurePercentage = 1.0 - arrivalPercentage;
		
		return new AirlineStatistic(airline, df.format(cargoPercentage), 
				df.format(arrivalPercentage), df.format(departurePercentage));
	}
}

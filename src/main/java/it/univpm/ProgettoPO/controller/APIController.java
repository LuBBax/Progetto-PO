package it.univpm.ProgettoPO.controller;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.univpm.ProgettoPO.exception.UnreachableAPIException;
import it.univpm.ProgettoPO.model.AirlineStatistic;
import it.univpm.ProgettoPO.model.Airport;
import it.univpm.ProgettoPO.model.Flight;
import it.univpm.ProgettoPO.service.AerodataBoxClient;
import it.univpm.ProgettoPO.util.Util;

/**
 *	Contiene le rotte che possono essere richiamate tramite client per richieste di tipo HTTP
 */
@RestController
public class APIController {
	
	AerodataBoxClient chiamata = new AerodataBoxClient();
	
	
	
	/*
	 * Path : /home, mostra i Path che l'utente ha a disposizione
	 */
	
	@RequestMapping("/home")
	public Vector <String> home() {
		
		return chiamata.istruction();
	}
	
	
	
	/**
	 * Route che permette di cercare degli aeroporti tramite l'API di AerodataBox.
	 * @return	Lista di aeroporti che contengono il termine di ricerca
	 */
	@GetMapping("airports")
	public @ResponseBody ResponseEntity<Object> searchAirports(@RequestParam(name = "locality",required = true) String search) {
		ResponseEntity<Object> response = null;
		
		if (search.length() < 3) {
			response = new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} 
		else {
			try {
				LinkedList<Airport> airports = AerodataBoxClient.getAirports(search);
				response = new ResponseEntity<Object>(airports, HttpStatus.OK);
			} catch(UnreachableAPIException e1) {
				response = new ResponseEntity<Object>(HttpStatus.SERVICE_UNAVAILABLE);
			} catch(Exception e2) {
				response = new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return response;
	}
	
	/**
	 * Route per elencare i voli per uno specifico aeroporto in una specifica data
	 * 
	 * @param airport Codice ICAO dell'aeroporto
	 * @param date Data per elencare i voli
	 * @param airlineFilter Filtro per airline
	 * @param categoryFilter Filtro per categoria ['arrival' o 'departure']
	 * @param beforeFilter Data massima del volo
	 * @param afterFilter Data minima del volo
	 * 
	 * @apiNote tutte le date devono essere passate nel formato "yyyy-MM-ddThh:mm"
	 * 
	 * @return Lista di voli delle successive 12h dalla data selezionata
	 */
	@GetMapping("flights/{airport}/{date}")
	public @ResponseBody ResponseEntity<Object> listFlights(@PathVariable String airport, @PathVariable String date,
			@RequestParam(name = "airline", required = false) String airlineFilter,
			@RequestParam(name = "category", required = false) String categoryFilter,
			@RequestParam(name = "before", required = false) String beforeFilter,
			@RequestParam(name = "after", required = false) String afterFilter) {
		
		ResponseEntity<Object> response = null;
		String format = "yyyy-MM-dd'T'HH:mm";
		
		if (validateListFlightsRequest(date, categoryFilter, beforeFilter, afterFilter)) {			
			try {
				Date specifiedDate = Util.getDateFromFormat(format, date);
				Date forwardDate = Util.addTimeToDate(specifiedDate, 12);
				
				LinkedList<Flight> flights = AerodataBoxClient.getFlights(airport, specifiedDate, forwardDate);
				
				// Filtraggio
				if (airlineFilter != null)
					flights = filterFlightsByAirline(flights, airlineFilter);
				
				if (categoryFilter != null) {
					flights = filterFlightsByCategory(flights, categoryFilter);
					
					if (beforeFilter != null && Util.getDateFromFormat(format, beforeFilter) != null)
						flights = filterFlightsByMaxDate(flights, Util.getDateFromFormat(format, beforeFilter));
					if (afterFilter != null && Util.getDateFromFormat(format, afterFilter) != null)
						flights = filterFlightsByMinDate(flights, Util.getDateFromFormat(format, afterFilter));
				}
					
				response = new ResponseEntity<Object>(flights, HttpStatus.OK);
			} catch(UnreachableAPIException e1) {
				response = new ResponseEntity<Object>("{ \"message\": \"Remote APIs unavaileble at the moment\" }", HttpStatus.SERVICE_UNAVAILABLE);
			} catch(Exception e2) {
				response = new ResponseEntity<Object>("{ \"error\": \"" + e2.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	
	
	/**
	 * Restituisce la lista di statistiche di tutte le airlines relative alla lista di voli ricercata
	 * 
	 * @param airport Codice ICAO dell'aeroporto
	 * @param date Data per elencare i voli
	 * 
	 * @return Lista di statistiche
	 */
	@GetMapping("flights/{airport}/{date}/stats/airlines")
	public @ResponseBody ResponseEntity<Object> listFlights(@PathVariable String airport, @PathVariable String date) {
		ResponseEntity<Object> response = null;
		String format = "yyyy-MM-dd'T'HH:mm";
		
		LinkedList<String> airlines = null;
		
		if (Util.isValidDateString(format, date)) {			
			try {
				Date specifiedDate = Util.getDateFromFormat(format, date);
				Date forwardDate = Util.addTimeToDate(specifiedDate, 12);
				
				LinkedList<Flight> flights = AerodataBoxClient.getFlights(airport, specifiedDate, forwardDate);
				airlines = Util.extractAirlinesByFlightsAirline(flights);
				
				LinkedList<AirlineStatistic> stats = new LinkedList<AirlineStatistic>();
				for(String airline: airlines)
					stats.add(AirlineStatistic.getFromFlightList(flights, airline));
					
				response = new ResponseEntity<Object>(stats, HttpStatus.OK);
			} catch(UnreachableAPIException e1) {
				response = new ResponseEntity<Object>("{ \"message\": \"Remote APIs unavaileble at the moment\" }", HttpStatus.SERVICE_UNAVAILABLE);
			} catch(Exception e2) {
				response = new ResponseEntity<Object>("{ \"error\": \"" + e2.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	/**
	 * Controlla che i parametri passati alla API per elencare i voli siano accettabili
	 *
	 * @param date
	 * @param categoryFilter
	 * @param beforeFilter
	 * @param afterFilter
	 * 
	 * @return Restituisce la validità dei parametri
	 */
	private boolean validateListFlightsRequest(String date, String categoryFilter, String beforeFilter, String afterFilter) {
		String format = "yyyy-MM-dd'T'HH:mm";
		boolean isValid = true;
		
		if(!Util.isValidDateString(format, date))
			isValid = false;
		
		if (categoryFilter == null && (afterFilter != null || beforeFilter != null))
			isValid = false;
		if(beforeFilter != null && !Util.isValidDateString(format, beforeFilter))
			isValid = false;
		if(afterFilter != null && !Util.isValidDateString(format, afterFilter))
			isValid = false;
		
		return isValid;
	}
	
	
	// Metodi per filtraggio
	private LinkedList<Flight> filterFlightsByAirline(LinkedList<Flight> source, String airline) {
		LinkedList<Flight> filtered = new LinkedList<Flight>();
				
		for (Flight f: source) {
			if (f.getAirline().toLowerCase().contains(airline.toLowerCase()))
				filtered.add(f);
		}
		
		return filtered;
	}
	
	private LinkedList<Flight> filterFlightsByMaxDate(LinkedList<Flight> source, Date date) {
		LinkedList<Flight> filtered = new LinkedList<Flight>();
		
		for (Flight f: source) {
			if (f.getScheduledTime().before(date))
				filtered.add(f);
		}
		
		return filtered;
	}
	
	private LinkedList<Flight> filterFlightsByMinDate(LinkedList<Flight> source, Date date) {
		LinkedList<Flight> filtered = new LinkedList<Flight>();
				
		for (Flight f: source) {
			if (f.getScheduledTime().after(date))
				filtered.add(f);
		}
		
		return filtered;
	}
	
	private LinkedList<Flight> filterFlightsByCategory(LinkedList<Flight> source, String category) {
		LinkedList<Flight> filtered = new LinkedList<Flight>();
				
		for (Flight f: source) {
			if (f.getCategory().toLowerCase().equals(category.toLowerCase()))
				filtered.add(f);
		}
		
		return filtered;
	}
}

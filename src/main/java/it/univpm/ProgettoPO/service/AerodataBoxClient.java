package it.univpm.ProgettoPO.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.univpm.ProgettoPO.exception.UnreachableAPIException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URL;
import java.net.URLConnection;

import it.univpm.ProgettoPO.model.Airport;
import it.univpm.ProgettoPO.model.Flight;
import it.univpm.ProgettoPO.util.Util;

public class AerodataBoxClient {
	
	Util util = new Util();
	
	
	/**
	 * Metodo per mostrare le varie possibilita' di chiamata all'API
	 * 
	 * @return Lista delle chiamate
	 */
	public Vector<String> istruction(){
		return util.getHome();
	}
	
	

	// 'X-RapidAPI-Host': 'aerodatabox.p.rapidapi.com',
    // 'X-RapidAPI-Key': '6555ba1d93mshaa240345e9ee46dp1940f0jsn234b0d282956'
	
	private static final String BASE_API_URL = "https://aerodatabox.p.rapidapi.com/";
	
	/**
	 * Dato un url, effettua una chiamata GET alla API remota
	 * @param url URL a cui chiedere i dati
	 * @return
	 * 
	 * @throws JSONException
	 * @throws UnreachableAPIException
	 */
	private static JSONObject getData(String url) throws JSONException, UnreachableAPIException {
		
		Util util = new Util();
		
				
		String key = util.getKey();
		
		
		OkHttpClient client = new OkHttpClient.Builder()
				.build();
		
		
		Request request = new Request.Builder()
				.url(url)
				.addHeader("Content-Type", "application/json")
				.addHeader("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
				.addHeader("X-RapidAPI-Key", key)
				.get()
				.build();
		
		JSONObject apiResult = null;
		
		try {
			Response response = client.newCall(request).execute();
			apiResult = new JSONObject(response.body().string());
		} catch (NullPointerException | IOException e) {
			throw new UnreachableAPIException();
		}
		
		return apiResult;
		
	}

	/**
	 * 
	 * @param airportName Nome per cercare l'aeroporto (nome o luogo)
	 * 
	 * @return Restituisce la lista di aeroporti che contengono il termine di ricerca
	 * 
	 * @throws JSONException Se la risposta delle API non è un json, viene sollevata un'eccezione
	 * @throws UnreachableAPIException Viene sollevata se il server remoto non è raggiungibile
	 */
	public static LinkedList<Airport> getAirports(String airportName) throws JSONException, UnreachableAPIException {
		LinkedList<Airport> airports = new LinkedList<>();
		
		String specificUrl = BASE_API_URL + "airports/search/term?q=" + airportName;
				
		JSONObject response = getData(specificUrl);
		JSONArray items = response.getJSONArray("items");
		
		for(int i = 0; i < items.length(); ++i) {
			try {
				System.out.println(items.getJSONObject(i).toString());
				
				airports.add(Airport.fromJson(items.getJSONObject(i)));
			}
			catch (Exception e) {}
		}
		
		return airports;
	}
	
	/**
	 * 
	 * @param icao Codice icao dell'aeroporto
	 * @param beginDate Data di inizio per ricerca voli
	 * @param endDate Data di fine per ricerca voli
	 * 
	 * @return Lista di voli
	 * 
	 * @throws JSONException Se la risposta delle API non è un json, viene sollevata un'eccezione
	 * @throws UnreachableAPIException Viene sollevata se il server remoto non è raggiungibile
	 */
	public static LinkedList<Flight> getFlights(String icao, Date beginDate, Date endDate) throws JSONException, UnreachableAPIException {
		LinkedList<Flight> flights = new LinkedList<Flight>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		
		String specificUrl =	BASE_API_URL + "flights/airports/icao/" + icao + "/" + 
								sdf.format(beginDate) + "/" + sdf.format(endDate);
				
		JSONObject response = getData(specificUrl);
		
		JSONArray items = response.getJSONArray("departures");
		for(int i = 0; i < items.length(); ++i) {
			try {
				flights.add(Flight.fromJson(items.getJSONObject(i), "departure"));
			}
			catch (Exception e) {}
		}
		
		items = response.getJSONArray("arrivals");
		for(int i = 0; i < items.length(); ++i) {
			try {
				flights.add(Flight.fromJson(items.getJSONObject(i), "arrival"));
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		return flights;
	}
}

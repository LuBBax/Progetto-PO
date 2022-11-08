package it.univpm.ProgettoPO.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe per mappare un aeroporto delle API esterne. Utilizza solo i dati fondamentali.
 */
public class Airport {
	private String name;
	private String icao;
	private String country;
	
	
	/**
	 * Getters e Setters
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcao() {
		return icao;
	}
	public void setIcao(String icao) {
		this.icao = icao;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}


	
	/**
	 * @param obj Oggetto JSON da convertire
	 * @return Un oggetto 'Airport'
	 * 
	 * @throws JSONException
	 */
	public static Airport fromJson(JSONObject obj) throws JSONException {
		Airport airport = new Airport();
		
		airport.setName(obj.getString("name"));
		airport.setIcao(obj.getString("icao"));
		airport.setCountry(obj.getString("countryCode"));
		
		return airport;
	}
}

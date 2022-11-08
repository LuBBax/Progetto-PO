/**
 * 
 */
package it.univpm.ProgettoPO;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.univpm.ProgettoPO.model.Aircraft;
import it.univpm.ProgettoPO.model.Flight;

class ModelTesting {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void aircraftTest() {
		Aircraft aircraft = null;
		
		JSONObject json = new JSONObject();
		try {
			json.put("reg", "my-value");
			json.put("model", "aircraft-model");
			
			aircraft= Aircraft.fromJson(json);
			if(aircraft.getModeS() != null)
				fail("Valore inatteso");
			
		} catch (JSONException e) {
			fail("Fallimento di serializzazione dell'oggetto");
		}
	}
	
	@Test
	void flightTest() {
		Flight flight = null;
		
		JSONObject json = new JSONObject();
		try {
			json.put("number", "my-value")
				.put("isCargo", false)
				.put("airline", new JSONObject()
					.put("name", "my-airline"))
				.put("movement", new JSONObject()
					.put("scheduledTimeLocal", "2022-05-24 16:13+02:00")
					.put("actualTimeLocal", "2022-05-24 17:11+02:00"));
			
			flight = Flight.fromJson(json, "catergory");
			
			if (flight.getAircraft() != null)
				fail("Aircraft dovrebbe essere null, ma è valorizzato");
			
			if (flight.getActualTime() == null)
				fail("La data dovrebbe essere valorizzata, ma è null");
			
		} catch (JSONException e) {
			fail("Fallimento di serializzazione dell'oggetto: " + e.getMessage());
		}
	}

}

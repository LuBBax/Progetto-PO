package it.univpm.ProgettoPO;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import junit.framework.TestCase;
import it.univpm.ProgettoPO.model.Aircraft;
import it.univpm.ProgettoPO.model.Flight;
import it.univpm.ProgettoPO.util.Util;

@SpringBootTest
public class ProgettoPoJunitTest {
	
	Util util = new Util();

	@Test
	void getKayTest() {
		
		assertEquals(util.getKey(),"6555ba1d93mshaa240345e9ee46dp1940f0jsn234b0d282956");
		
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
				fail("Aircraft dovrebbe essere null, ma e' valorizzato");
			
			if (flight.getActualTime() == null)
				fail("La data dovrebbe essere valorizzata, ma e' null");
			
		} catch (JSONException e) {
			fail("Fallimento di serializzazione dell'oggetto: " + e.getMessage());
		}
	}

	
	
	
	
}





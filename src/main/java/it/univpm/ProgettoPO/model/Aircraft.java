package it.univpm.ProgettoPO.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe per mappare un aereo all'interno dei voli delle API esterne. Utilizza solo i dati fondamentali.
 */
public class Aircraft {
	private String reg;
	private String modeS;
	private String model;
	
	
	/**
	 * Getters e Setters
	 */
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public String getModeS() {
		return modeS;
	}
	public void setModeS(String modeS) {
		this.modeS = modeS;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	
	/**
	 * Estrapola un oggetto 'Aircraft' da un JSON
	 * 
	 * @param obj Oggetto JSON da convertire
	 * @return Un oggetto 'Aircraft'
	 * 
	 * @throws JSONException
	 */
	public static Aircraft fromJson(JSONObject obj) throws JSONException {
		Aircraft aircraft = new Aircraft();
		
		aircraft.setReg((obj.has("reg")) ? obj.getString("reg") : null);
		aircraft.setModeS((obj.has("modeS")) ? obj.getString("modeS") : null); //modeS
		aircraft.setModel((obj.has("model")) ? obj.getString("model") : null); //model
		
		return aircraft;
	}
}

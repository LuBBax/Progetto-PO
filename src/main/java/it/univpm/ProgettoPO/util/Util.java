package it.univpm.ProgettoPO.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import it.univpm.ProgettoPO.model.Flight;

import org.springframework.stereotype.Component;

@Component
public class Util implements Service {
	
	
	public Vector<String> getHome()  {
		
		String homeFile="C://Data//Data//WorkspaceJava//Progetto_v1//PROGETTO-PO//src//main/resources//home.txt";
		
		Vector<String> home= new Vector<String>();    
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(homeFile));
			
			do { 
				home.add(reader.readLine());
			
			} while((reader.readLine()) != null); 
		
			 reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return home;
	}
	
	
	/**
	 * Metodo che consente di leggere la key dal txt ed utilizzarla per le richieste http
	 * 
	 * 
	 * @return la key in formato stringa
	 */
	
	public String getKey() {
		
		File file_key = new File ("C://Data//Data//WorkspaceJava//Progetto_v1//PROGETTO-PO//src//main//resources//api-key.txt");
		String key="";
		
		try {
			BufferedReader buf = new BufferedReader(new FileReader (file_key));
			key = buf.readLine();
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Problema nel try getKey_FileNotFound");
		} catch (IOException e) {System.out.println("Problema nel try getKey_IOException");}
		
		return 	key;
		
	}
		
	

	
	
	/**
	 * Valida una stringa contenente una data per un determinato formato
	 * 
	 * @param format Formato della data
	 * @param date Data da validare
	 * 
	 * @return Se la string data rispetta il formato specificato
	 */
	public static boolean isValidDateString(String format, String date) {
		boolean isValid = true;
		
		Date dt = getDateFromFormat(format, date);
		if (dt == null)
			isValid = false;

		return isValid;
	}
	
	/**
	 * Converte una string in data se corrisponde al formato specificato
	 * 
	 * @param format Formato
	 * @param date Stringa da convertire
	 * 
	 * @return Data convertita; null se non rispetta il formato
	 */
	public static Date getDateFromFormat(String format, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date dt;
		dt = sdf.parse(date, new ParsePosition(0));
		
		return dt;
	}

	/**
	 * Aggiunge delle ore alla data
	 * 
	 * @param date Data da aggiornare
	 * @param hours Numero di ore da aggiungere
	 * 
	 * @return
	 */
	public static Date addTimeToDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hours);
	    return calendar.getTime();
	}
	
	public static LinkedList<String> extractAirlinesByFlightsAirline(LinkedList<Flight> source) {
		LinkedList<String> airlines = new LinkedList<String>();
		
		for(Flight f: source) {
			if (!airlines.contains(f.getAirline()))
				airlines.add(f.getAirline());
		}
		
		return airlines;
	}

}

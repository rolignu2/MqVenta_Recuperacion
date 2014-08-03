package com.example.mqventa_recuperacion;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class id {
		
	public static String Id_recuperacion = null;
	
	
	public static int diferenciaEnDias(Date fechaMayor,Date fechaMenor){
		GregorianCalendar date1 = new GregorianCalendar();
		date1.setTime(fechaMenor); //dateIni es el objeto Date
		GregorianCalendar date2 = new GregorianCalendar();
		date2.setTime(fechaMayor); //dateFin es el objeto Date
		int rango = 0;
		if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
			rango = date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR);
		}
		else {

		int diasAnyo = date1.isLeapYear(date1.get(Calendar.YEAR)) ? 366 : 365;

		int rangoAnyos = date2.get(Calendar.YEAR)- date1.get(Calendar.YEAR);

		rango = (rangoAnyos * diasAnyo) + (date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR));
		}
		return rango;
	}
	
}

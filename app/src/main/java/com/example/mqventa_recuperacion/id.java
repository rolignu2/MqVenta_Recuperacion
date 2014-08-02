package com.example.mqventa_recuperacion;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class id {
		
	public static String Id_recuperacion = null;
	
	
	public static int diferenciaEnDias(Date fechaMayor,Date fechaMenor){
		/* CREAMOS LOS OBJETOS GREGORIAN CALENDAR PARA EFECTUAR LA RESTA */
		GregorianCalendar date1 = new GregorianCalendar();
		date1.setTime(fechaMenor); //dateIni es el objeto Date
		GregorianCalendar date2 = new GregorianCalendar();
		date2.setTime(fechaMayor); //dateFin es el objeto Date
		int rango = 0;
		/* COMPROBAMOS SI ESTAMOS EN EL MISMO AÑO */
		if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
			rango = date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR);
		}
		else {
		/* SI ESTAMOS EN DISTINTO AÑO COMPROBAMOS QUE EL AÑO DEL DATEINI NO SEA BISIESTO
		* SI ES BISIESTO SON 366 DIAS EL AÑO
		* SINO SON 365
		*/
		int diasAnyo = date1.isLeapYear(date1.get(Calendar.YEAR)) ? 366 : 365;
		/* CALCULAMOS EL RANGO DE AÑOS */
		int rangoAnyos = date2.get(Calendar.YEAR)- date1.get(Calendar.YEAR);
		/* CALCULAMOS EL RANGO DE DIAS QUE HAY */
		rango = (rangoAnyos * diasAnyo) + (date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR));
		}
		return rango;
	}
	
}

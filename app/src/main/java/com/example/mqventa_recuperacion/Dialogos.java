package com.example.mqventa_recuperacion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialogos {
	
	
	@SuppressWarnings("deprecation")
	public void SimpleDialogo(Context contexto ,String mensaje , String titulo)
	{
		final AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(contexto).create();
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensaje);
		alertDialog.setButton3("Cerrar", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				alertDialog.cancel();
			}
			
		});
		alertDialog.show();
	

	}
	
	

}

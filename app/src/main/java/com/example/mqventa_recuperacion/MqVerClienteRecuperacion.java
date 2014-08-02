package com.example.mqventa_recuperacion;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;

public class MqVerClienteRecuperacion extends Activity {

	private final Handler Delegado = new Handler();
	private TabHost Tabulador;
	private String ID_CLIENTE = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*Codigo para obtener permisos mysql y android*/
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		setContentView(R.layout.activity_mq_ver_cliente_recuperacion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mq_ver_cliente_recuperacion, menu);
	    this.ID_CLIENTE = id.Id_recuperacion;
	    this.ConfiguracionTab();
		Thread hilo = new Thread(){
			public void run()
			{
				Delegado.post(Inicializar);
			}
		};
		hilo.start();
		return true;
	}
	
	/*importante aca se coloca toda configuracion de las pestañas o tabs */
	private void ConfiguracionTab()
	{
		Resources res = super.getResources();
		 
		Tabulador=(TabHost)findViewById(android.R.id.tabhost);
		Tabulador.setup();
		
		//configuracion del primero tabulador para obtencion de deudas
		TabHost.TabSpec spec=Tabulador.newTabSpec("Control");
		spec.setContent(R.id.tab2);
		spec.setIndicator("",
		    res.getDrawable(R.drawable.shopping32));
		Tabulador.addTab(spec);
		 
		
		//configuramos el segundo tabulador que se llamara datos personales
	    spec=Tabulador.newTabSpec("Personales");
		spec.setContent(R.id.tab1);
		spec.setIndicator("",
		    res.getDrawable(R.drawable.contactcard32));
		Tabulador.addTab(spec);
		 
	
		//iniciar desde el tabulador cero
		Tabulador.setCurrentTab(0);
	}
	
	private Runnable Inicializar = new Runnable()
	{
		public void run()
		{
			
		}
	};
	
	


}

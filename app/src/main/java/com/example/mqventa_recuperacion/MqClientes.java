package com.example.mqventa_recuperacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

public class MqClientes extends Activity {

	
	private android.widget.ListView ListaCliente;
	
	@SuppressWarnings("unused")
	private String[] ArrayClientes;
	private Conexion conn;
	private android.widget.TabHost Tabulador;
	private android.widget.Button CmdRegistrar;
	
	private final Handler Delegado = new Handler();
	private final Handler Delegado2 = new Handler();
	private final Context contexto = this;
	
	private android.widget.Button boton;
	private android.widget.EditText txtnombre;
	private android.widget.EditText txtapellido;
	private android.widget.EditText txtdui;
	private android.widget.EditText txtnit;
	private android.widget.EditText txtelefono;
	private android.widget.EditText txtgiro;
	private android.widget.EditText txtdireccion;
	
	private Clientes clientes;

	//private ProgressDialog progressDialog = new ProgressDialog(this);
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Codigo para obtener permisos mysql y android*/
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		setContentView(R.layout.activity_mq_clientes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mq_clientes, menu);
		this.Tabulador = (android.widget.TabHost)super.findViewById(android.R.id.tabhost);
		this.ListaCliente = (android.widget.ListView) super.findViewById(R.id.ListaClientes_Cliente);
		this.CmdRegistrar = (android.widget.Button) super.findViewById(R.id.cmd_registrar_cliente);
		this.ConfiguracionTab();
		Thread hilo = new Thread()
		{
			public void run()
			{
				Delegado.post(Iniciar);
			}
		};
		hilo.start();
		this.Load();
		return true;
	}
	

	private void ConfiguracionTab()
	{
		@SuppressWarnings("unused")
		Resources res = super.getResources();
		 
		Tabulador=(TabHost)findViewById(android.R.id.tabhost);
		Tabulador.setup();
		
		//configuramos el primer tabulador que se llamara datos personales
		TabHost.TabSpec spec=Tabulador.newTabSpec("Personales");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Registro");
		Tabulador.addTab(spec);
		 
		//configuracion del segundo tabulador para obtencion de deudas
		spec=Tabulador.newTabSpec("Control");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Clientes");
		Tabulador.addTab(spec);
		 
		//iniciar desde el tabulador cero
		Tabulador.setCurrentTab(0);
		
	}
	
	public void Load()
	{
		
		 	progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("Guardando...");
			progressDialog.setMessage("Espere un momento...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(1000);
			progressDialog.setProgress(0);
		    progressDialog.setCancelable(false);

			this.boton = (android.widget.Button) super.findViewById(R.id.cmd_registrar_cliente);
			this.txtnombre=(android.widget.EditText)super.findViewById(R.id.txtnombre_clientes);
			this.txtapellido=(android.widget.EditText)super.findViewById(R.id.txtapellido_clientes);
			this.txtdui=(android.widget.EditText)super.findViewById(R.id.txtdui_clientes);
			this.txtnit=(android.widget.EditText)super.findViewById(R.id.txtnit_clientes);
			this.txtelefono=(android.widget.EditText)super.findViewById(R.id.txtelefono_clientes);
			this.txtgiro=(android.widget.EditText)super.findViewById(R.id.txtgiro_clientes);
			this.txtdireccion=(android.widget.EditText)super.findViewById(R.id.txtdireccion_clientes);
			
			this.boton.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
		
				public void onClick(View v) {
					if(validacion()){
						progressDialog.show();
						Thread hilo = new Thread()
						{
							public void run()
							{
								Delegado2.post(GuardarDatos);
							}
						};
						hilo.start();
					}
				}
			});
			
	}
	
	private boolean validacion()
	{
		Dialogos diag = new Dialogos();
		if(this.txtnombre.getText().length()==0 
				|| this.txtapellido.getText().length()==0)
		{
			diag.SimpleDialogo(this,
					"El nombre o apellido no deben estar vacios", 
					"Opps!");
			return false;
		}
		else if(this.txtelefono.getText().length()==0)
		{
			diag.SimpleDialogo(this,
					"El campo telefonico debe de contener un numero", 
					"Opps!");
			return false;
		}
			
		return true;
	}

	private Runnable GuardarDatos = new Runnable()
	{
		public void run()
		{
			try {

				clientes = new Clientes();
				String sql = "INSERT INTO clientes ( nombres, apellidos, dui, nit, fiscal, telefono, giro, otro)" +
						" VALUES('" + txtnombre.getText().toString() + "','"
						+ txtapellido.getText().toString() + "','"
						+ txtdui.getText().toString() + "','"
						+ txtnit.getText().toString() + "','"
						+ "nulo" + "','"
						+ txtelefono.getText().toString() + "','"
						+ txtgiro.getText().toString() + "','"
						+ txtdireccion.getText().toString() + "')";
				progressDialog.setProgress(600);
				if(clientes.SetNewCliente(sql))
				{
					progressDialog.setTitle("Exito !!");
					progressDialog.setMessage("Cliente Guardado con exito...");
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progressDialog.setProgress(1000);
				}
				else
				{
					progressDialog.setTitle("Opps!!");
					progressDialog.setMessage("Servidor no disponible (Intente mas tarde) ");
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progressDialog.setProgress(1000);
				}
				progressDialog.cancel();
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (SQLException e) {
			}
			progressDialog.setProgress(1000);
			progressDialog.cancel();
		}
	};
	
	private Runnable Iniciar = new Runnable()
	{
		public void run(){
			
		try {
			conn = new Conexion();
			conn.AbrirConexion();
			String sql = "Select * from clientes";
			ResultSet rs = conn.Get_Consulta(sql);
			int cont=0;
			ArrayList<String> ArregloCliente = new  ArrayList<String>();
			ArrayAdapter<String> adaptador;
			while(rs.next())
			{
				cont++;
				String obj = rs.getString("id_cliente") + ") " + rs.getString("nombres")+ " , " + rs.getString("apellidos");
			    ArregloCliente.add(obj);
			}
			if(cont >=1){
				
			    ArrayClientes = new String[ArregloCliente.size()];
			    
			    for(int i =0; i < ArregloCliente.size();i++)
			    	ArrayClientes[i] = ArregloCliente.get(i).toString();
			    
				adaptador = new ArrayAdapter<String>(contexto, android.R.layout.simple_expandable_list_item_1, ArrayClientes);
				ListaCliente.setAdapter(adaptador);
			}
			else
			{
				ArrayClientes = new String[]
						{
							"No Existen Clientes."
						};
				adaptador = new ArrayAdapter<String>(contexto, android.R.layout.simple_expandable_list_item_1 , ArrayClientes);
			}
			
			conn.CerrarConexion();
			
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (SQLException e) {
			/*AlertDialog ad = new AlertDialog.Builder(this).create();  
			ad.setCancelable(false); // This blocks the 'BACK' button  
			ad.setMessage(e.getMessage().toString());  
			ad.setButton("OK", new DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			        dialog.dismiss();                      
			    }  
			});  
			ad.show();  */
		}
			
		}
		
	};
	

}

package com.example.mqventa_recuperacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class MqVentaMain extends Activity {

	
	private TextView LblTitulo;
	private RelativeLayout FrmPrincipal;
	private Button cmd_recuperacion;
	private Button cmd_clientes;
	private Button cmd_productos;
	private Button cmd_crearRecuperacion;

	
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	private String Titulo = "Venta y Recuperacion Version Movil";
	
	private Context contexto = this;
	private Handler delegado = new Handler();
	private Conexion conn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mq_venta_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mq_venta_main, menu);
		/*Codigo para obtener permisos mysql y android*/
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		this.Inizializar();
		return true;
	}
	
	
	public void Inizializar()
	{
		 this.LblTitulo = (TextView) super.findViewById(R.id.lbltitulo);
		 this.LblTitulo.setText("Cargando Noticias ...");
		 this.expListView = (ExpandableListView) findViewById(R.id.Menu_lista_expandible);
		 this.FrmPrincipal = (RelativeLayout)super.findViewById(R.id.FrmPrincipal);
		 Thread hilo = new Thread()
		 {
			 public void run()
			 {
				 delegado.post(PrepararLista);
			 }
		 };
		 hilo.start();
		 //eventos para botones 
		 this.Eventos_Click();
	}


    /*prepara la lista de datos a mostrar
    *
    * */
	private Runnable PrepararLista = new Runnable() {
		
		public void run(){
			List<String>ClienteMora = new ArrayList<String>();
			List<String>ClienteProximo = new ArrayList<String>();
			int mora_count =0;
			int prox_count=0;
			try {
				conn = new Conexion();
				conn.AbrirConexion();
				String sql ="Select clientes.id_cliente as id , clientes.nombres as nombre , " +
						"clientes.apellidos as apellido , " +
						"control.fecha_pago as fecha FROM clientes INNER JOIN control" +
						" ON clientes.id_cliente=control.id_cliente";
				ResultSet rs = conn.Get_Consulta(sql);
				Calendar c = Calendar.getInstance();
				Date fecha1 =c.getTime();
				while(rs.next())
				{
					Date fecha2 = rs.getDate("fecha");
					int dias = id.diferenciaEnDias(fecha1, fecha2);
					String cliente = rs.getString("nombre" ) + " " + rs.getString("apellido");
					if(dias>=0){
						ClienteMora.add(cliente);
						mora_count++;
					}
					else if(dias<=-5){
						ClienteProximo.add(cliente);
						prox_count++;
					}
					
				}
				conn.CerrarConexion();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<String>>();
 
			if(mora_count==0)
				listDataHeader.add("Clientes en Mora");
			else
				listDataHeader.add("(" + mora_count + ") Clientes en Mora");
			
			if(prox_count == 0)
				listDataHeader.add("Clientes Proximo a Pagar");
			else
				listDataHeader.add("(" + prox_count +  ") Clientes Proximo a Pagar");


			List<String> Mora = new ArrayList<String>();
			if(ClienteMora.isEmpty())
					Mora.add("Sin clientes en mora");
			else
			{
				for(int i=0; i < ClienteMora.size() ; i++)
					Mora.add(ClienteMora.get(i).toString());
			}
			
			List<String> MoraProxima = new ArrayList<String>();
			if(ClienteProximo.isEmpty())
					MoraProxima.add("Sin clientes proximos a pagar");
			else
			{
				for(int i=0; i < ClienteProximo.size() ; i++)
					MoraProxima.add(ClienteProximo.get(i).toString());
			}
			
    
			listDataChild.put(listDataHeader.get(0), Mora);
			listDataChild.put(listDataHeader.get(1), MoraProxima);
			
			listAdapter = new ExpandableListAdapter(contexto, listDataHeader, listDataChild);
			expListView.setAdapter(listAdapter);
			LblTitulo.setText(Titulo);
		}
    };

	

    /*Eventos inciales para los botones*/
	private void Eventos_Click()
	{
		this.cmd_recuperacion = (Button)super.findViewById(R.id.cmd_recuperacion);
		this.cmd_clientes = (Button)super.findViewById(R.id.cmd_clientes);
		this.cmd_productos = (Button)super.findViewById(R.id.cmd_productos);
		this.cmd_crearRecuperacion = (Button)super.findViewById(R.id.cmd_crear_recup_main);
		
		this.cmd_clientes.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Ejecutar_Clientes(v);
			}
		});
		
		this.cmd_recuperacion.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Ejecutar_Recuperacion(v);
			}
		});

		this.cmd_crearRecuperacion.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Ejecutar_CreacionRecuperacion(v);
			}
		});

        this.cmd_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ejecutar_Productos(view);
            }
        });
	
	}
	
	public void Ejecutar_CreacionRecuperacion(View view)
	{
		Intent i = new Intent(this, CRecuperacion_MQ.class);
		startActivity(i);
	}
	
	public void Ejecutar_Recuperacion(View view) {
		Intent i = new Intent(this, MqRecuperacion.class);
		startActivity(i);
	}
	
	public void Ejecutar_Clientes(View view) {
		Intent i = new Intent(this, MqClientes.class);
		startActivity(i);
	}

    public void Ejecutar_Productos(View view)
    {
        Intent i = new Intent(this,MqProductos.class);
        startActivity(i);
    }

}

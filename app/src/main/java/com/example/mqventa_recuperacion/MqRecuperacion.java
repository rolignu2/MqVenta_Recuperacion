package com.example.mqventa_recuperacion;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;



@SuppressLint("SimpleDateFormat")
public class MqRecuperacion extends Activity {

	private android.widget.ListView ListaCliente;
	private android.widget.ProgressBar BarraProgreso;
	//obsoleto... cambio por un elemento nuevo
	//private android.widget.EditText TxtBuscar;
	private SearchView TxtBuscar; //se cambio por este searchview...
	
	private final Handler Delegado = new Handler();
	private Context contexto;
	
	ArrayList<Lista_entrada> Lista;
	private Conexion conn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*Codigo para obtener permisos mysql y android*/
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		setContentView(R.layout.activity_mq_recuperacion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mq_recuperacion, menu);
		this.ListaCliente = (android.widget.ListView) super.findViewById(R.id.ListClientes);
		this.BarraProgreso = (android.widget.ProgressBar)super.findViewById(R.id.barra_progreso_recuperacion);
		//this.TxtBuscar = (android.widget.EditText)super.findViewById(R.id.txtbuscar_recuperacion_mq);
		this.TxtBuscar = (android.widget.SearchView) super.findViewById(R.id.TxtRecuperacion_Buscador);
		this.BarraProgreso.animate();
		this.BarraProgreso.setVisibility(android.widget.ProgressBar.VISIBLE);
		this.contexto = this;
		Thread hilo = new Thread(){
			public void run(){
				MysqlLoad();
				Delegado.post(Iniciar);
			}
		};
		hilo.start();
		this.Eventos();
		return true;
	}
	
	//conexion standar mysql 
	//con esta conexion en una funcion realiza operaciones especificas en mysql
	private void MysqlLoad()
	{
		try {
			conn = new Conexion();
			conn.AbrirConexion();
			
			String sql = "Select clientes.id_cliente , clientes.nombres , clientes.apellidos ," +
			" control.fecha_pago, control.abonos , control.nuevo_saldo , control.pagado"
			+ " FROM clientes INNER JOIN control ON clientes.id_cliente=control.id_cliente ";
			
			ResultSet rs = conn.Get_Consulta(sql);
			Lista = new ArrayList<Lista_entrada>();
			int img_select =0;
			while(rs.next())
			{
				String PiePagina = "";
				String PrincipioPag = rs.getString("clientes.id_cliente") + ") " 
				+ rs.getString("clientes.nombres")
				+ " , " 
				+ rs.getString("clientes.apellidos");
				Date fecha = rs.getDate("control.fecha_pago");
				Calendar c = Calendar.getInstance();
				Date fecha1 =c.getTime();
				int rango_fechas = id.diferenciaEnDias(fecha1, fecha);
				
				SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
				PiePagina = "Expira El " + df1.format(fecha) + " Deuda $" + rs.getString("control.nuevo_saldo");
				if( rango_fechas >= 0)
				{
					img_select = R.drawable.pagoexpirado;
					PiePagina = "Abono $"+ rs.getShort("control.abonos") +  " Deuda $" + rs.getString("control.nuevo_saldo");
				}
				else if(rs.getInt("control.pagado")==1)
					img_select= R.drawable.pagado;	
				else
					img_select = R.drawable.pendiente;

				Lista.add(new Lista_entrada(img_select, PrincipioPag ,PiePagina));
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
		
	}
	
	
	//evento click para la lista en el formulario
	//todos los eventes se van a llamar en el inicializador
	@SuppressLint("DefaultLocale")
	private void Eventos()
	{
		ListaCliente.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
				Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion); 
                String texto_inicial = elegido.get_textoEncima();
                String[] codigo =texto_inicial.split("\\)");
                com.example.mqventa_recuperacion.id.Id_recuperacion = codigo[0];
                VerClientesRecuperacion();
			}
        });
		
		/*this.TxtBuscar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(TxtBuscar.getText().toString().contains("Buscar"))
						TxtBuscar.setText("");
				return false;
			}
		});*/
		
		
		this.TxtBuscar.setOnQueryTextListener(new OnQueryTextListener()
		{

			private String textoBuscar;
			@SuppressWarnings("unused")
			private Lista_entrada datos;
			private ArrayList<Lista_entrada> subLista;
			
			public void llamada(String arg0)
			{
				subLista = new ArrayList<Lista_entrada>();
				textoBuscar = arg0;
				for (int i = 0; i < Lista.size(); i++) {
					datos = Lista.get(i);
					if(datos.get_textoEncima().toUpperCase().indexOf(textoBuscar.toUpperCase()) != -1)
					{
						subLista.add(datos);
					}
				}
				
				ListaCliente.setAdapter(new Lista_adaptador(contexto,R.layout.activity_mq_entrada__recuperacion, subLista){
					@Override
					public void onEntrada(Object entrada, View view) {
						if (entrada != null) {
				            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior); 
				            if (texto_superior_entrada != null) 
				            	texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima()); 

				            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior); 
				            if (texto_inferior_entrada != null)
				            	texto_inferior_entrada.setText(((Lista_entrada) entrada).get_textoDebajo()); 

				            ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen); 
				            if (imagen_entrada != null)
				            	imagen_entrada.setImageResource(((Lista_entrada) entrada).get_idImagen());
				        }
					}
					
				});
			}

			@SuppressLint("DefaultLocale")
			@Override
			public boolean onQueryTextChange(String arg0) {
				llamada(arg0);
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				llamada(arg0);
				return false;
			}
			
		}
		);
	
			

		
	}
	
	//iniciar el formulario de recuperacion cliente individual
	private void VerClientesRecuperacion()
	{
		Intent i = new Intent(this, MqVerClienteRecuperacion.class);
   		startActivity(i);
	}
	

	//Hilo en cual espera respuesta del servidor para obtener la informacion inicial
	private Runnable Iniciar = new Runnable()
	{
		public void run(){
			if(Lista.size() >=1){
				
				ListaCliente.setAdapter(new Lista_adaptador(contexto,R.layout.activity_mq_entrada__recuperacion, Lista){
					@Override
					public void onEntrada(Object entrada, View view) {
						if (entrada != null) {
				            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior); 
				            if (texto_superior_entrada != null) 
				            	texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima()); 

				            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior); 
				            if (texto_inferior_entrada != null)
				            	texto_inferior_entrada.setText(((Lista_entrada) entrada).get_textoDebajo()); 

				            ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen); 
				            if (imagen_entrada != null)
				            	imagen_entrada.setImageResource(((Lista_entrada) entrada).get_idImagen());
				        }
					}
					
				});
			    
				BarraProgreso.setVisibility(android.widget.ProgressBar.GONE);
			}
			else
			{
				 
			}
		}
	};

	

}

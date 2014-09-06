/**
 * 
 * CLASE CLIENTE ROLANDO ARRIAZA 
 * VERSION 0.1 (ACTIVA)
 * 
 * 
 * 
 * */

package com.example.mqventa_recuperacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import android.os.StrictMode;


public class Clientes extends Conexion {
	

	private String ID=null;
	private Map<String, String> CDic;


    /**
     * obtiene permisos de red para mysql
     * */
	private void AndroidMysql()
	{
		if (android.os.Build.VERSION.SDK_INT >= 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	}
	
	public Clientes() throws SQLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		super();
		AndroidMysql();
	}
	
	public Clientes(String id) throws SQLException, ClassNotFoundException,
	InstantiationException, IllegalAccessException {
		super();
		this.ID = id;
		AndroidMysql();
	}

    /**
     * establece el id
     * */

	public void Set_ID(String id)
	{
		this.ID = id;
	}

    /**
     * obtiene el id
     * */
	
	public String Get_ID()
	{
		return this.ID;
	}

    /**
     * obtiene el id y el nombre del cliente por medio de un diccionario
     * hash
     * */
	
	public Map<String , String> Get_ClienteById()
	{
		this.CDic= new HashMap<String , String>();
		try {
			super.AbrirConexion();
			String sql = "";
            ResultSet rs = super.Get_Consulta(sql);
            super.CerrarConexion();
            while(rs.next())
            {
                String id = rs.getString("id");
                String value = rs.getString("nombre");
                CDic.put(id,value);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this.CDic;
	}
	
	/**
	 * funcion en la cual agrega un cliente por medio de una arreglo
	 * no terminada 
	 * */
	public boolean SetNewCliente(String[] valores)
	{
		
		try {
			super.AbrirConexion();
			
			
			super.CerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * funcion en la cual agrega un cliente por medio de 
	 * una sentencia sql 
	 * version 0.1
	 * 
	 * */

	public boolean SetNewCliente(String sql)
	{
		boolean ret = false;
		
		try {
			super.AbrirConexion();
			ret = super.Get_Consulta_update(sql);
			super.CerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * function devuelve un resultset 
	 * de todos los valores de los clientes
	 * */
	
	public ResultSet Get_Clientes() 
	{
		
		ResultSet resultado = null;
		try {
			super.AbrirConexion();
			String sql = "Select * from clientes";
			resultado = super.Get_Consulta(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
	}
}

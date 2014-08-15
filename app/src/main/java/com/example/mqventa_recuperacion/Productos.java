package com.example.mqventa_recuperacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by rolandoantonio on 08-05-14.
 */

public class Productos extends Conexion {


    private int Count = 0;

    private int Counter(ResultSet rs) throws SQLException {
        int c =0;
        while (rs.next()) c++;
        return c;
    }



    /**
     * CONTRUCTOR DE LA CLASE PRODUCTOS
     * */
    public Productos() throws SQLException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
            super();
    }


    /**
     * agrega un valor del producto mediante una sentencia sql
     * */
    public boolean ADD_Prod(String sql) throws SQLException {
        boolean ok = false;
        super.AbrirConexion();
        ok = super.Get_Consulta_update(sql);
        super.CerrarConexion();
        return ok;
    }

    //obtiene un resultset de todos los productos
    //esos son sus nombres , categoria y su id
    public ResultSet Get_Prod_NameID() throws SQLException {
        String sql ="SELECT id_producto as id , producto as nombre ," +
                " categoria as categoria FROM productos";
        super.AbrirConexion();
        ResultSet rs;
        rs = super.Get_Consulta(sql);
        this.Count = Counter(rs);
        rs.first();
        return rs;
    }

    //elimina un producto y devuelve true si se elimino
    public boolean DeleteProdID(String id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto=" + id  ;
        super.AbrirConexion();
        return  super.Get_Consulta_update(sql);
    }


    //obtiene el conteo de los productos
    public int GetCount()
    {
        return this.Count;
    }


    //cierra la conexion bdd de un producto
    /**
     * Deprecado , se puede utilizar la herencia tambien
     * */
    public void CloseProd() {
        try {
            super.CerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<String> GetProductById(String id) throws SQLException {
        List<String> lista = new ArrayList<String>();
        String sql ="SELECT * FROM productos WHERE id_producto=" + id;
        super.AbrirConexion();
        ResultSet rs = super.Get_Consulta(sql);
        if(rs.next())
        {
            lista.add(rs.getString("id_producto"));
            lista.add(rs.getString("producto"));
            lista.add(rs.getString("envase"));
            lista.add(rs.getString("cantidad"));
            lista.add(rs.getString("precio_unitario"));
            lista.add(rs.getString("precio_iva"));
            lista.add(rs.getString("total"));
            lista.add(rs.getString("categoria"));
        }
        super.CerrarConexion();
        return lista;
    }
}

package com.example.mqventa_recuperacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


/**
 * Created by rolandoantonio on 08-05-14.
 */

public class Productos extends Conexion {


    private int Count = 0;

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

    public boolean DeleteProdID(String id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto=" + id  ;
        super.AbrirConexion();
        return  super.Get_Consulta_update(sql);
    }

    private int Counter(ResultSet rs) throws SQLException {
        int c =0;
        while (rs.next()) c++;
        return c;
    }

    public int GetCount()
    {
        return this.Count;
    }

    public void CloseProd() {
        try {
            super.CerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

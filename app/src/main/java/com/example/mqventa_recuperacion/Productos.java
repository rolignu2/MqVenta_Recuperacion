package com.example.mqventa_recuperacion;

import java.sql.SQLException;
import java.util.Map;


/**
 * Created by rolandoantonio on 08-05-14.
 */
public class Productos extends Conexion {



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
}

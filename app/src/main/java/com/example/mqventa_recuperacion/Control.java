package com.example.mqventa_recuperacion;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by rolandoantonio on 09-03-14.
 */
public class Control extends Conexion {


    public Control() throws SQLException,
            ClassNotFoundException, InstantiationException, IllegalAccessException
    {

    }

    public boolean CrearNuevoControl(String id_cliente , Date fecha_control ,
                                     String bono_inicial , List<Object>... ListProductos ) throws SQLException {


        String sql = "";


        try {
            super.AbrirConexion();

            if(ListProductos == null || ListProductos.length == 0)
            {
                sql = "INSERT INTO CONTROL";
                boolean b = super.Get_Consulta_update(sql);
                super.CerrarConexion();
                return b;
            }
            else
            {
                boolean is_add = false;
                for(int i = 0 ; i < ListProductos.length ; i++)
                {
                    sql = "INSERT INTO CONTROL";
                    is_add = super.Get_Consulta_update(sql);
                    if(!is_add) break;
                }
                return is_add;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

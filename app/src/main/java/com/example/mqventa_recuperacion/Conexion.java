package com.example.mqventa_recuperacion;



import com.mysql.jdbc.Statement;
import com.mysql.jdbc.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conexion {
    
   
    private Connection conexion;
    private Statement consulta;
    
    private final String  User = "creadb1cod";
    private final String Password = "Jkl45@23uiAA";
    private final String Bdd = "creadb1cod";
    private final String server = "creadb1cod.db.12451247.hostedresource.com";
    
    
    public Conexion() throws SQLException, 
            ClassNotFoundException, 
            InstantiationException, 
            IllegalAccessException {
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        this.conexion = (Connection) DriverManager.getConnection("jdbc:mysql://" + server +"/" + Bdd, User, Password);
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
    
    public Connection Get_conexion()
    {
        return this.conexion;
    }
    
    
    public void AbrirConexion() throws SQLException
    {
        if(this.conexion.isClosed())
        	this.conexion = (Connection) DriverManager.getConnection("jdbc:mysql://" + server +"/" + Bdd, User, Password);
    }
    
    public void CerrarConexion() throws SQLException
    {
       if(!this.conexion.isClosed()) {
            this.conexion.close();
        } 
    }
    
    public ResultSet Get_Consulta(String sql) throws SQLException
    {
        this.consulta = (Statement) this.conexion.createStatement();
        ResultSet rs = this.consulta.executeQuery (sql);
        return rs;
    }


    public int Get_consulta_count(String sql) throws SQLException
    {
        this.consulta = (Statement) this.conexion.createStatement();
        ResultSet Est = this.consulta.executeQuery(sql);
        int cont =0;
        while(Est.next())
            cont++;
        return cont;
    }
    
     public boolean Get_Consulta_update(String sql) throws SQLException
    {
        this.consulta = (Statement) this.conexion.createStatement();
        int Est = this.consulta.executeUpdate(sql);
        return Est >=1;
    }
    
     public boolean Get_consulta_imagen(String id , String nombre , File foto ) throws SQLException, FileNotFoundException
     {
         PreparedStatement ps = null;
         FileInputStream fis = null;
         int estado =0;
         try{
            String INSERT_PICTURE = "insert into imagenes(id_imagen, nombre, photo) values (?, ?, ?)";
            this.conexion.setAutoCommit(false);
            fis = new FileInputStream(foto);
            ps = conexion.prepareStatement(INSERT_PICTURE);
            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.setBinaryStream(3, fis, (int) foto.length());
            estado = ps.executeUpdate();
            conexion.commit();
         }finally {
             try {
                 ps.close();
                 fis.close();
             } catch (IOException ex) {
                 Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
                 return false;
             }
         }
        return estado == 1;
     }
    
    
}

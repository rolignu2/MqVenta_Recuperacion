package com.example.mqventa_recuperacion;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MqVerProducto extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mq_ver_producto);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mq_ver_producto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    public static class PlaceholderFragment extends Fragment {

        //variables para obtener el view del objeto
        private ImageView ProdImg;
        private TextView txt_nombre_prod;
        private TextView txt_envase;
        private TextView txt_precioUnitario;
        private TextView txt_precioIva;
        private TextView txt_Total;
        private TextView txt_cantidad;

        //obtener el view en raiz y contexto
        private Context contexto;
        private View rootView;
        private Handler delegate = new Handler();

        //obtener los datos del producto
        private List<String> lista_prod = new ArrayList<String>();
        public static String id_producto = "";


        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_mq_ver_producto, container, false);
            contexto = rootView.getContext();
            CargarView();
            return rootView;
        }

        private void CargarView()
        {
           ProdImg = (ImageView) rootView.findViewById(R.id.VerProdIMG);
           ProdImg.setImageResource(R.drawable.otros);
           txt_nombre_prod = (TextView) rootView.findViewById(R.id.TxtVerProd_Producto);
           txt_envase= (TextView) rootView.findViewById(R.id.TxtVerProd_Envase);
           txt_precioUnitario= (TextView) rootView.findViewById(R.id.TxtVerProd_PrecioUnit);
           txt_precioIva= (TextView) rootView.findViewById(R.id.TxtVerProd_PrecioIva);
           txt_Total= (TextView) rootView.findViewById(R.id.TxtVerProd_Total);
           txt_cantidad= (TextView) rootView.findViewById(R.id.TxtVerProd_Cantidad);

            Thread hilo = new Thread(new Runnable() {
                @Override
                public void run() {
                    MysqlLoadProd();
                    delegate.post(LoadDelegate);
                }
            });
            hilo.start();

        }

        private void MysqlLoadProd()
        {
            lista_prod = new ArrayList<String>();
            try {
                Productos prod = new Productos();
                lista_prod = prod.GetProductById(id_producto);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        /**
         *  lista.add(rs.getString("id_producto"));
         lista.add(rs.getString("producto"));
         lista.add(rs.getString("envase"));
         lista.add(rs.getString("cantidad"));
         lista.add(rs.getString("precio_unitario"));
         lista.add(rs.getString("precio_iva"));
         lista.add(rs.getString("total"));
         lista.add(rs.getString("categoria"));
         *
         * */

        public Runnable LoadDelegate = new Runnable() {
            @Override
            public void run() {

                    if(lista_prod.size() <= 0) return;
                    else
                    {
                        switch (Integer.parseInt(lista_prod.get(7)))
                        {
                            case 1:
                                ProdImg.setImageResource(R.drawable.liquido);
                                break;
                            case 2:
                                ProdImg.setImageResource(R.drawable.solido);
                                break;
                            case 3:
                                ProdImg.setImageResource(R.drawable.polvo);
                                break;
                            case 4:
                                ProdImg.setImageResource(R.drawable.otros);
                                break;
                        }

                        txt_nombre_prod.setText("Producto " + lista_prod.get(1));
                        txt_envase.setText("Envase " + lista_prod.get(2));
                        txt_cantidad.setText("Cantidad (" +lista_prod.get(3) + ") Unidad(es)");
                        txt_precioUnitario.setText("Precio Unitario $" + lista_prod.get(4));
                        txt_precioIva.setText("Precio Iva $" +lista_prod.get(5));
                        txt_Total.setText("Total $" + lista_prod.get(6));

                    }
            }
        };


    }

}

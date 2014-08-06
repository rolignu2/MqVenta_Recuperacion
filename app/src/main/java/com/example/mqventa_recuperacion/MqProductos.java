package com.example.mqventa_recuperacion;

import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MqProductos extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mq_productos);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mq_productos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        //variables para agregar los productos
        private TextView txt_nombre;
        private TextView txt_envase;
        private TextView txt_cantidad;
        private TextView txt_precio;
        private TextView txt_precio_iva;
        private ProgressDialog progressDialog;
        private final android.os.Handler Delegado = new android.os.Handler();
        private AlertDialog.Builder alert;
        //fin de las variables add productos

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            //arguamento en el cual verifica el numero de la seccion
            int arg = getArguments().getInt(ARG_SECTION_NUMBER);
            //cambia los datos o parametros de acuerdo a la positions del placeholder
            //ademas con el View podremos cambiar el layout a utilizar en este caso cada placeholder cambiara el tipo de layout
            return this.ChangePlace(arg , container, inflater);
        }


        public View ChangePlace(int seccion ,ViewGroup container , LayoutInflater inflater)
        {
            View rootView;
            rootView = inflater.inflate(R.layout.fragment_mq_productos, container, false);
            switch(seccion)
            {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_mq_productos, container, false);
                    this.GetAddProd(rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_mq_productos1, container, false);
                    this.GetViewProd(rootView);
                    break;
                default:
                    break;

            }

            return rootView;
        }

        /**
         * se creara el menu en el cual obtendremos el registro de un producto
         * se agregaran los controles en el view
         * */
        private void GetAddProd(View rootView)
        {
            TextView textView = (TextView) rootView.findViewById(R.id.txtnext_productos);
            textView.setText("AGREGA UN NUEVO PRODUCTO");

            Button cmd_add = (Button) rootView.findViewById(R.id.cmd_add_prod);
            txt_nombre = (TextView) rootView.findViewById(R.id.txt_nombre_prod);
            txt_envase = (TextView) rootView.findViewById(R.id.txt_envase_prod);
            txt_cantidad = (TextView) rootView.findViewById(R.id.txt_cantidad_prod);
            txt_precio = (TextView) rootView.findViewById(R.id.txt_precio_prod);
            txt_precio_iva = (TextView) rootView.findViewById(R.id.txt_precioIVA_prod);

            progressDialog = new ProgressDialog(rootView.getContext());
            progressDialog.setTitle("Guardando...");
            progressDialog.setMessage("Espere un momento...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1000);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);

            alert = new AlertDialog.Builder(rootView.getContext());
            alert.setMessage("Opps!! Registro no guardado, puede que sea fallo de conexion");
            alert.setTitle("Error");
            alert.setPositiveButton("OK", null);

            cmd_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      progressDialog.show();
                     Thread hilo = new Thread()
                     {
                         public void run()
                         {

                             Delegado.post(GuardarProductos);
                         }
                     };
                    hilo.start();
                }

            });

            txt_precio.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent arg) {
                    if(arg.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                    {
                        double precio = Double.parseDouble(txt_precio.getText().toString());
                        double iva = precio + (precio *0.13);
                        txt_precio_iva.setText(String.valueOf(iva));
                    }
                    return false;
                }
            });

        }

        public Runnable GuardarProductos;

        {
            GuardarProductos = new Runnable() {
                @Override
                public void run() {
                    progressDialog.setProgress(100);
                    progressDialog.setMessage("Abriendo Conexion ...");

                    int cantidad = Integer.parseInt(txt_cantidad.getText().toString());
                    double precio = Double.parseDouble(txt_precio.getText().toString());
                    double total = precio * cantidad;

                    String sql = "INSERT INTO productos (envase,producto,cantidad," +
                            "precio_unitario," +
                            "precio_iva," +
                            "total) VALUES ('"
                            + txt_envase.getText().toString() + "','"
                            + txt_nombre.getText().toString() + "'," + txt_cantidad.getText().toString() + ","
                            + txt_precio.getText().toString() + "," + txt_precio_iva.getText().toString() + ","
                            + String.valueOf(total) + ")";
                    try {
                        Productos prod = new Productos();
                        progressDialog.setProgress(500);
                        boolean ok = prod.ADD_Prod(sql);
                        progressDialog.setProgress(900);
                        if(!ok)
                        {
                            progressDialog.cancel();
                            alert.show();
                        }

                        else{
                            progressDialog.cancel();
                            alert.setMessage("El producto se agrego a la base de datos");
                            alert.setTitle("Producto Agregado");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        progressDialog.cancel();
                        alert.show();
                    } catch (ClassNotFoundException e) {
                    } catch (java.lang.InstantiationException e) {
                    } catch (IllegalAccessException e) {
                    }

                }
            };
        }


        private void GetViewProd(View rootView)
        {
            TextView textView = (TextView) rootView.findViewById(R.id.txtnext_productos_info);
            textView.setText("INFORMACION PRODUCTOS");
        }


    }

}

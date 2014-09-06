package com.example.mqventa_recuperacion;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.*;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UsrRecuperacion extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usr_recuperacion);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.usr_recuperacion, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {



        private static final String ARG_SECTION_NUMBER = "section_number";
        private static List<List<String>> Clientes = new ArrayList<List<String>>();
        private static List<String> ClienteSelect = new ArrayList<String>();
        private List<String> ListaProductos = new ArrayList<String>();

        private View rootView;
        private int seccion = 0;
        private Handler delegado = new Handler();
        private Handler delegado2 = new Handler();
        private ProgressDialog pDialog;

        private ListView lista_clientes;
        private ExpandableListView lista_prod;
        private TextView txt_cliente;
        private DatePicker date_cliente;
        private TextView abono_inicial;
        private Button cmd_guardar_recuperacion;


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

            seccion = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (seccion)
            {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_usr_recuperacion, container, false);
                    CargaClientes();
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_usr_recuperacion_crear, container, false);
                    CrearRecuperacion();
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_usr_recuperacion, container, false);
                    break;

            }

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((UsrRecuperacion) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private void CargaClientes()
        {

            pDialog = new ProgressDialog(rootView.getContext());
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Cargando lista clientes ...");
            pDialog.setCancelable(true);
            pDialog.setMax(100);
            pDialog.show();
            Thread hilo = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            LoadClientMysql();
                            delegado.post(CrearListaClientes);
                        }
                    }
            );
            hilo.start();
        }

        private void LoadClientMysql()
        {
            Clientes clientes = null;
            ResultSet rs = null;
            try {
                clientes = new Clientes();
                rs = clientes.Get_Clientes();
                while(rs.next())
                {
                    List<String> lista = new ArrayList<String>();
                    lista.add(rs.getString("id_cliente"));
                    lista.add(rs.getString("nombres"));
                    lista.add(rs.getString("apellidos"));
                    lista.add(rs.getString("dui"));
                    lista.add(rs.getString("nit"));
                    lista.add(rs.getString("fiscal"));
                    lista.add(rs.getString("telefono"));
                    lista.add(rs.getString("giro"));
                    lista.add(rs.getString("otro"));
                    Clientes.add(lista);
                }
                clientes.CerrarConexion();
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


        private Runnable CrearListaClientes = new Runnable()
        {
            public void run(){


                if(Clientes.size() != 0 || Clientes != null){

                    String[] ArrayClientes = new String[Clientes.size()];
                    int i = 0;
                    for (List<String> perfil: Clientes)
                    {

                      String id = perfil.get(0);
                      String nombre = perfil.get(1);
                      String apellido = perfil.get(2);
                      ArrayClientes[i] = id + "." + nombre + "," + apellido;
                      i++;
                    }

                    lista_clientes = (ListView) rootView.findViewById(R.id.lista_clientes_pefiles);

                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(rootView.getContext() ,
                            android.R.layout.simple_expandable_list_item_1 , ArrayClientes );
                    lista_clientes.setAdapter(adaptador);

                    lista_clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                              String item = (String) adapterView.getItemAtPosition(i);
                              MenuSeleccion(item , view);
                        }
                    });

                }

                pDialog.setProgress(100);
                pDialog.cancel();

            }

        };

        private void MenuSeleccion(final String selected , View view)
        {

            int menu = R.menu.menu_select_cliente;
            PopupMenu popupMenu = new PopupMenu(rootView.getContext(), view);
            popupMenu.getMenuInflater().inflate(menu, popupMenu.getMenu());
            final String id_prod = selected.substring(0,1);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {

                    ClienteSelect = new ArrayList<String>();
                    for(List<String> c : Clientes)
                    {
                       if(Integer.parseInt(c.get(0)) == Integer.parseInt(id_prod))
                       {
                           ClienteSelect = c;
                           break;
                       }
                    }

                    Toast.makeText(rootView.getContext(), "Seleccionado: " + selected , Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            popupMenu.show();
        }




        private void CrearRecuperacion()
        {
            if(ClienteSelect.size() == 0)
            {
               Dialogos diag = new Dialogos();
               diag.SimpleDialogo(rootView.getContext() , "No se ha seleccionado cliente" , "Sin cliente");
               return;
            }

            txt_cliente = (TextView) rootView.findViewById(R.id.txt_cliente_recuperacion);
            txt_cliente.setText(ClienteSelect.get(1) + "," + ClienteSelect.get(2) );

            date_cliente = (DatePicker) rootView.findViewById(R.id.date_fecha_pago);
            abono_inicial = (TextView) rootView.findViewById(R.id.txt_abonoinicial_recuperacion);
            cmd_guardar_recuperacion = (Button)rootView.findViewById(R.id.cmd_guardar_recuperacion);
            lista_prod = (ExpandableListView)rootView.findViewById(R.id.lista_productos_recuperacion);

            cmd_guardar_recuperacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int day =  date_cliente.getDayOfMonth();
                    int month = date_cliente.getMonth();
                    int year =  date_cliente.getYear();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);



                }
            });

            Thread hilo = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CargarProductos();
                        delegado2.post(MostrarProductos);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            hilo.start();

        }


        private void CargarProductos() throws ClassNotFoundException,
                SQLException,
                java.lang.InstantiationException,
                IllegalAccessException {

                Productos prod = new Productos();
                ResultSet rs = prod.Get_Prod_NameID();

                if(rs != null )
                    while (rs.next())
                    {
                        String make = rs.getString("id") + ")" + rs.getString("nombre");
                        ListaProductos.add(make);
                    }
            prod.CerrarConexion();
        }

        private Runnable MostrarProductos = new Runnable()
        {
            @Override
            public void run() {
                if(ListaProductos.size() == 0) return;
                HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                List<String> listDataHeader = new ArrayList<String>();

                listDataHeader.add("Productos ");
                listDataChild.put(listDataHeader.get(0) , ListaProductos);

                ExpandableCheckAdapter listAdapter = new ExpandableCheckAdapter(rootView.getContext(), listDataHeader, listDataChild);
                lista_prod.setAdapter(listAdapter);
            }
        };




    }

}

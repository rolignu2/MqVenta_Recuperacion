package com.example.mqventa_recuperacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.*;
import static android.widget.PopupMenu.OnMenuItemClickListener;

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
            // Show 2 total pages.
            return 2;
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
        private ExpandableListView lista_prod_add;
        private AlertDialog.Builder alert;

        private ExpandableListAdapter listAdapter;
        private ExpandableListView expListView;
        private List<String> listDataHeader;
        private HashMap<String, List<String>> listDataChild;
        private int categoria = 4;
        private List<String> lista_categoria = new ArrayList<String>();
        private  int[] ImgProd;
        //fin de las variables add productos

        //variables para modificar , eliminar y ver productos
        private GridView grilla_productos;
        private final android.os.Handler handler = new android.os.Handler();
        private Context NewContext ;
        private List<String> CadProd = new ArrayList<String>();
        //fin variables mod productos

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

            //obtenemos los widgets
            Button cmd_add = (Button) rootView.findViewById(R.id.cmd_add_prod);
            txt_nombre = (TextView) rootView.findViewById(R.id.txt_nombre_prod);
            txt_envase = (TextView) rootView.findViewById(R.id.txt_envase_prod);
            txt_cantidad = (TextView) rootView.findViewById(R.id.txt_cantidad_prod);
            txt_precio = (TextView) rootView.findViewById(R.id.txt_precio_prod);
            txt_precio_iva = (TextView) rootView.findViewById(R.id.txt_precioIVA_prod);
            lista_prod_add = (ExpandableListView) rootView.findViewById(R.id.lista_prod_add);


            //procces dialog para guardar en la base de datos
            //este process dialog inicia
            progressDialog = new ProgressDialog(rootView.getContext());
            progressDialog.setTitle("Guardando...");
            progressDialog.setMessage("Espere un momento...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(1000);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            lista_categoria = new ArrayList<String>();


            listDataHeader.add("Categoria");
            lista_categoria.add("Liquido");
            lista_categoria.add("Solido");
            lista_categoria.add("Polvo");
            lista_categoria.add("Otros");

            listDataChild.put(listDataHeader.get(0) , lista_categoria);

            listAdapter = new ExpandableListAdapter(rootView.getContext() , listDataHeader , listDataChild);
            lista_prod_add.setAdapter(listAdapter);

            alert = new AlertDialog.Builder(rootView.getContext());
            alert.setMessage("Opps!! Registro no guardado, puede que sea fallo de conexion");
            alert.setTitle("Error");
            alert.setPositiveButton("OK", null);

            cmd_add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                progressDialog.show();
                    new Thread(new Runnable()
                     {
                         public void run()
                         {
                             Delegado.post(GuardarProductos);
                         }
                     }).start();
                }

            });

            txt_precio.setOnKeyListener(new OnKeyListener() {
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

            lista_prod_add.setOnGroupClickListener(new  ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView arg0, View arg1,
                                            int groupPosition, long arg3) {

                    return false;
                }
            });

           lista_prod_add.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
               @Override
               public boolean onChildClick(ExpandableListView parent, View view, int i, int i2, long l) {
                     String cat = lista_categoria.get(i2).toString();
                     categoria = i2+1;
                     Toast.makeText(view.getContext() ,
                           "Ha seleccionado " + cat , Toast.LENGTH_SHORT).show();
                   return false;
               }
           });

        }

        /**
         * se crea un runnable o un hilo para guardar
         * los productos dentro de una base de datos en la web
         * */

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
                            "total , categoria) VALUES ('"
                            + txt_envase.getText().toString() + "','"
                            + txt_nombre.getText().toString() + "'," + txt_cantidad.getText().toString() + ","
                            + txt_precio.getText().toString() + "," + txt_precio_iva.getText().toString() + ","
                            + String.valueOf(total) +  "," + String.valueOf(categoria) + ")";
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


        //funcion para mandar el menu item
        public void MenuProducto(View view , String item)
        {
            showPopupMenu(view, R.menu.menu_prod , item);
        }


        //optenemos el popupmenu para aparicion en el evento touch
        private void showPopupMenu(View view, int menu ,  final String SelItem)
        {

            PopupMenu popupMenu = new PopupMenu(NewContext, view);
            popupMenu.getMenuInflater().inflate(menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {

                    switch (item.getItemId())
                    {
                        case R.id.menu_opt1:
                            String[] split_prod = SelItem.split(" ");
                            final String id_prod = split_prod[0];
                            MqVerProducto.PlaceholderFragment.id_producto = id_prod;
                            VerProducto();
                            break;
                        case R.id.menu_opt2:

                            break;
                        case R.id.menu_opt3:
                            EliminarProd(SelItem);
                            break;
                        case R.id.menu_opt4:
                            break;
                    }

                    Toast.makeText(NewContext, SelItem , Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            popupMenu.show();
        }

        private void VerProducto()
        {
            Intent i = new Intent(NewContext ,MqVerProducto.class);
            super.startActivity(i);
        }


        private void EliminarProd(final String producto)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewContext);
            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    String[] split_prod = producto.split(" ");
                    final String id_prod = split_prod[0];

                    final ProgressBar progreso_del = new ProgressBar(NewContext);

                    Thread HiloP = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            progreso_del.post(new Runnable() {
                                @Override
                                public void run() {
                                    progreso_del.setProgress(0);
                                    progreso_del.setMax(100);
                                }
                            });

                            for(int i=1; i<=10; i++) {
                                progreso_del.post(new Runnable() {
                                    public void run() {
                                        progreso_del.incrementProgressBy(10);
                                    }
                                });
                            }

                            boolean is_delete = false;
                            try {
                               is_delete = new Productos().DeleteProdID(id_prod);
                                if(is_delete)
                                {
                                   //aca va a ir el refresco de la grilla
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            LoadMysqlProd();
                                            handler.post(MostrarProductos);
                                        }
                                    }
                                    ).start();
                                }
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

                    });
                    HiloP.start();

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            AlertDialog alerta = builder.create();
            alerta.setTitle("Eliminar producto..");
            alerta.setMessage("¿Desea eliminar el producto " + producto + "?");
            alerta.show();

        }


        private void GetViewProd(final View rootView)
        {
            TextView textView = (TextView) rootView.findViewById(R.id.txtnext_productos_info);
            grilla_productos = (GridView) rootView.findViewById(R.id.grilla_productos);
            textView.setText("INFORMACION PRODUCTOS");
            NewContext = rootView.getContext();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LoadMysqlProd();
                    handler.post(MostrarProductos);
                }
            }

            ).start();

            //evento on click de la grilla
            grilla_productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selec = (String) adapterView.getItemAtPosition(i);//obtenemos el item selecionado por medio de la pocision
                    MenuProducto(view , selec); //obtiene el popup construido
                }
            });


        }


        public void LoadMysqlProd()
        {
            CadProd = new ArrayList<String>();
            ImgProd = new int[1];
            Productos prod = null;
            ResultSet rs = null;
            int count_prod = 0;

            try {
                prod = new Productos();
                rs = prod.Get_Prod_NameID();
                count_prod = prod.GetCount();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if(rs != null) {
                try {

                    if(count_prod != 0 )
                        ImgProd = new int[count_prod];

                    int i =0;
                    while (rs.next()) {

                        String nombre = rs.getString("nombre");
                        String id = rs.getString("id");
                        int categoria = rs.getInt("categoria");

                        CadProd.add(id + " " + nombre);
                        switch (categoria) {
                            case 1:
                                ImgProd[i] = R.drawable.liquido;
                                break;
                            case 2:
                                ImgProd[i] = R.drawable.solido;
                                break;
                            case 3:
                                ImgProd[i] = R.drawable.polvo;
                                break;
                            case 4:
                                ImgProd[i] = R.drawable.otros;
                                break;
                            default:
                                ImgProd[i] = R.drawable.otros;
                                break;
                        }
                        i++;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(prod != null)
                    prod.CloseProd();
            }
        }

        public Runnable MostrarProductos = new Runnable() {
            @Override
            public void run() {

                if(CadProd.size() >= 1)
                {
                    MqGrid grid;
                    grid = new MqGrid(NewContext ,CadProd , ImgProd);
                    grilla_productos.setAdapter(grid);
                }
            }
        };

    }

}

package com.example.appcompra;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.adapters.MenuAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.ProductosConID;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView menu;
    RecyclerView recyclerView;
    ListaAdapter adapter;
    Usuario usuario;
    Button botonNueva;
    String nombreNuevaLista;
    Toolbar toolbar;
    TextView titulo;
    Lista listaSeleccionada;
    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    protected boolean working=true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socket= QueryUtils.getSocket();
        try {
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PeticionesThread p=new PeticionesThread();
        p.setDaemon(true);
        p.start();
        Singleton.getInstance().setHiloComunicacion(p);

        usuario = (Usuario) getIntent().getExtras().getSerializable("Usuario");
        QueryUtils.setUsuario(usuario);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        botonNueva = findViewById(R.id.boton_nueva_lista);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titulo = toolbar.findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = findViewById(R.id.header);
        TextView nombreUsuario = headView.findViewById(R.id.drawer_nombre);
        nombreUsuario.setText(usuario.getNombre());
        TextView emailUsuario = headView.findViewById(R.id.drawer_email);
        emailUsuario.setText(usuario.getEmail());
        ImageView imagenUsuario = headView.findViewById(R.id.drawer_imagen);
        Picasso.get().load(usuario.getUrlImagenPerfil()).into(imagenUsuario);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MenuAdapter adapter = new MenuAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //menu de abajo
        menu = findViewById(R.id.menu_bottom);
        menu.setOnNavigationItemSelectedListener(navListener);
        menu.getMenu().getItem(3).setChecked(true);
        botonNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaListaPopup();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    menu.getMenu().getItem(0).setChecked(false);
                }
                if (position != 5) {
                    menu.getMenu().getItem(position).setChecked(true);
                    prevMenuItem = menu.getMenu().getItem(position);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setCurrentItem(2);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_despensa:
                            viewPager.setCurrentItem(0);
                            titulo.setText("Despensa");
                            break;
                        case R.id.menu_listas:
                            viewPager.setCurrentItem(1);
                            titulo.setText("Listas");
                            break;
                        case R.id.menu_home:
                            viewPager.setCurrentItem(2);
                            titulo.setText("Inicio");
                            break;
                        case R.id.menu_productos:
                            viewPager.setCurrentItem(3);
                            titulo.setText("Productos");
                            break;
                        case R.id.menu_recetas:
                            viewPager.setCurrentItem(4);
                            titulo.setText("Recetas");
                            break;
                    }
                    TextView subtitle = toolbar.findViewById(R.id.subtitle);
                    subtitle.setVisibility(View.GONE);
                    LinearLayout linearToolbar = toolbar.findViewById(R.id.linearToolbar);
                    linearToolbar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    return false;
                }
            };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void updateUI(TreeSet<Lista> m) {
        /*productos.clear();
        productos.addAll(m);
        */
        adapter = new ListaAdapter(m, this, R.layout.item_row_listas, this, new ListaAdapter.OnItemClickListener() {
            @Override
            public void onBorrarLista(Lista l) {
                borrarListaPopup(l);
            }

            @Override
            public void onCompartirLista(Lista l) {
                compartirListaPopup(l);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void crearNuevaListaPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_crear_lista, null);
        final AutoCompleteTextView editText = view.findViewById(R.id.crear_lista_editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp = view.findViewById(R.id.botonAceptarPopup);

        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreNuevaLista = editText.getText().toString();
                if (!nombreNuevaLista.isEmpty()) {
                    //peticionNuevaLista();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getBaseContext(), "Introduce un nombre para la lista", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void borrarListaPopup(Lista l) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_confirmacion, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        listaSeleccionada = l;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp = view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp = view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        botonCancelarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void compartirListaPopup(Lista l) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_compartir, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AutoCompleteTextView editText = view.findViewById(R.id.editText);
        final Spinner spinnerRoles = view.findViewById(R.id.spinnerRoles);
        List<String> valoresSpinner = new ArrayList<>();
        valoresSpinner.add("Ninguno");
        valoresSpinner.add("Administrador");
        valoresSpinner.add("Participante");
        valoresSpinner.add("Espectador");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(), R.layout.spinner_item, valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        listaSeleccionada = l;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp = view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp = view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        botonCancelarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public class PeticionesThread extends Thread{

        PeticionesThread(){

        }

        @Override
        public void run() {
            while (working) {
                try {
                    Log.e("enviado","durmiendo");
                    Thread.sleep(6000000);

                } catch (InterruptedException e) {
                    procesarPeticiones();
                    rellenarColecciones();
                }
                Thread.interrupted();
            }
        }
        public synchronized void procesarPeticiones(){
            while(Singleton.getInstance().getPeticionesEnviar().size()>0) {
                try {
                    String salida = Singleton.getInstance().getPeticionMaxPrioridad();

                    out.println(salida);
                    Log.e("procesar", salida + " size:" + Singleton.getInstance().getPeticionesEnviar().size());

                    String entrada = in.readLine();
                    while(Singleton.getInstance().getRespuestasServidor())
                    
                    Log.e("procesar", entrada);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        public synchronized void rellenarColecciones(){
            while(Singleton.getInstance().getRespuestasServidor().size()>0){
                String entrada=Singleton.getInstance().getRespuestaMaxPrioridad();
                if(entrada.contains(Constants.SEPARATOR))
                    if(!entrada.split(Constants.SEPARATOR)[0].equals(Constants.PETICIONES_INDIRECTAS_ENVIADAS))
                        procesarEntrada(entrada);
            }
        }
        /*  Constants.COMPARTIR_LISTA_CORRECTA;
            Constants.CATEGORIAS_RESPUESTA_CORRECTA;
            Constants.PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA;
            Constants.PRODUCTOS_LISTA_CORRECTA;
            Constants.LISTAS_RESPUESTA_CORRECTA;
            Constants.PRODUCTOS_DESPENSA_CORRECTA;
            Constants.CREACION_NUEVA_LISTA_CORRECTA;*/

        public void procesarEntrada(String entrada){
            String codigoRespuesta=entrada.split(Constants.SEPARATOR)[0];
            //Esta peticion va a ser directa asi que comprobamos su codigo entre el de las peticiones directas
            switch (codigoRespuesta){
                case Constants.COMPARTIR_LISTA_CORRECTA:
                    Log.e("procesar","lista compartida, el id de la lista es"+entrada.split(Constants.SEPARATOR)[1]);
                    break;
                case Constants.CATEGORIAS_RESPUESTA_CORRECTA:
                    Singleton.getInstance().setCategorias(QueryUtils.categoriasJson(entrada.split(Constants.SEPARATOR)[1]));
                    break;
                case Constants.PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA:
                    ProductosConID productosCategoria =QueryUtils.tipoProductosJson(entrada);
                    Singleton.getInstance().añadirNuevosProductosCategoria(productosCategoria.getId(), productosCategoria.getProductosConID());
                    break;
                case Constants.PRODUCTOS_LISTA_CORRECTA:
                    ProductosConID productosLista =QueryUtils.productosLista(entrada);
                    Singleton.getInstance().añadirNuevosProductosCategoria(productosLista.getId(), productosLista.getProductosConID());
                    break;
                case Constants.LISTAS_RESPUESTA_CORRECTA:
                    Singleton.getInstance().setListas(QueryUtils.listasJson(entrada.split(Constants.SEPARATOR)[1]));
                    break;
                case Constants.PRODUCTOS_DESPENSA_CORRECTA:
                    break;
                case Constants.CREACION_NUEVA_LISTA_CORRECTA:

                    break;
                default:
                    Log.e("procesar","Codigo de respuesta desconocido");
                break;
            }
        }
    }
}

package com.example.appcompra;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
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
import com.example.appcompra.clases.Categoria;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.ProductosConID;
import com.example.appcompra.clases.ProductosListaConID;
import com.example.appcompra.clases.Receta;
import com.example.appcompra.clases.RecetasConID;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.models.CategoriaViewModel;
import com.example.appcompra.models.DespensaViewModel;
import com.example.appcompra.models.ListasViewModel;
import com.example.appcompra.models.ProductoViewModel;
import com.example.appcompra.models.ProductosListaViewModel;
import com.example.appcompra.models.RecetaViewModel;
import com.example.appcompra.utils.Notificacion;
import com.example.appcompra.utils.Peticion;
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
    CustomViewPager viewPager;
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
    DrawerLayout drawerLayout;
    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    protected boolean working=true ;
    protected ProductoViewModel modelProductos;
    protected CategoriaViewModel categoriaViewModel;
    protected ListasViewModel listasViewModel;
    protected ProductosListaViewModel productosListaViewModel;
    protected DespensaViewModel despensaViewModel;
    protected RecetaViewModel recetaViewModel;

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

        PeticionesThread p2=new PeticionesThread();
        p2.setDaemon(true);
        p2.start();


        RespuestasThread p=new RespuestasThread();
        p.setDaemon(true);
        p.start();

        PedirNotificacionesThread p3=new PedirNotificacionesThread();
        p3.setDaemon(true);
        p3.start();


        Singleton.getInstance().setHiloComunicacion(p2);

        this.categoriaViewModel= ViewModelProviders.of(this).get(CategoriaViewModel.class);
        this.modelProductos= ViewModelProviders.of(this).get(ProductoViewModel.class);
        this.listasViewModel= ViewModelProviders.of(this).get(ListasViewModel.class);
        this.productosListaViewModel= ViewModelProviders.of(this).get(ProductosListaViewModel.class);
        this.despensaViewModel= ViewModelProviders.of(this).get(DespensaViewModel.class);
        this.recetaViewModel= ViewModelProviders.of(this).get(RecetaViewModel.class);

        usuario = (Usuario) getIntent().getExtras().getSerializable("Usuario");
        QueryUtils.setUsuario(usuario);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        botonNueva = findViewById(R.id.boton_nueva_lista);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titulo = toolbar.findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
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
        viewPager = (CustomViewPager) findViewById(R.id.swipePager);
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

        viewPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
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
                @SuppressLint("ResourceAsColor")
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                    toolbar.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gradient));
                    menu.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.gradient));
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
        drawer.closeDrawers();

        return true;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CustomViewPager getViewPager() {
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

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }
    public ProductoViewModel getModelProductos() {
        return modelProductos;
    }

    public void setModelProductos(ProductoViewModel modelProductos) {
        this.modelProductos = modelProductos;
    }

    public CategoriaViewModel getCategoriaViewModel() {
        return categoriaViewModel;
    }

    public void setCategoriaViewModel(CategoriaViewModel categoriaViewModel) {
        this.categoriaViewModel = categoriaViewModel;
    }

    public ListasViewModel getListasViewModel() {
        return listasViewModel;
    }

    public void setListasViewModel(ListasViewModel listasViewModel) {
        this.listasViewModel = listasViewModel;
    }

    public ProductosListaViewModel getProductosListaViewModel() {
        return productosListaViewModel;
    }

    public DespensaViewModel getDespensaViewModel() {
        return despensaViewModel;
    }


    public class PeticionesThread extends Thread{

        @Override
        public void run() {
            while (working) {
                try {
                    Log.e("enviado","durmiendo");
                    Thread.sleep(6000000);
                } catch (InterruptedException e) {
                    procesarPeticiones();
                    Thread.interrupted();
                }

            }
        }
        public synchronized void procesarPeticiones(){
            while(Singleton.getInstance().getPeticionesEnviar().size()>0) {
                String salida = Singleton.getInstance().getPeticionMaxPrioridad();
                out.println(salida);
                Log.e("procesar", salida + " size:" + Singleton.getInstance().getPeticionesEnviar().size());
            }
        }

    }

    public class PedirNotificacionesThread extends Thread{

        @Override
        public void run() {
            while (working) {
                try {
                    Log.e("pedir","esperando");
                    Thread.sleep(15000);
                    pedirNotificaciones();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        public synchronized void pedirNotificaciones(){
            Peticion p=new Peticion(Constants.PEDIR_NOTIFICACIONES,QueryUtils.getUsuario().getId());
            String salida=p.getStringPeticion();
            out.println(salida);
            Log.e("procesar", salida );
        }

    }

    public class RespuestasThread extends Thread{

        @Override
        public void run() {
            while (working) {
                rellenarColecciones();
            }
        }

        public synchronized void rellenarColecciones(){
            try {
                procesarEntrada(in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void procesarEntrada(String entrada){
            String codigoRespuesta=entrada.split(Constants.SEPARATOR)[0];
            //Esta peticion va a ser directa asi que comprobamos su codigo entre el de las peticiones directas
            try {
                switch (codigoRespuesta) {
                    case Constants.NOTIFICACIONES_PROCESADA_CORRECTA:
                        Log.e("procesar", entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().setNotificaciones(QueryUtils.procesarNotificacion(entrada.split(Constants.SEPARATOR)[1]));
                        procesarNotificaciones();
                        break;
                    case Constants.PEDIR_NOTIFICACIONES_CORRECTA:
                        Log.e("procesar", entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().setNotificaciones(QueryUtils.procesarNotificacion(entrada.split(Constants.SEPARATOR)[1]));
                        procesarNotificaciones();
                        break;
                    case Constants.COMPARTIR_LISTA_CORRECTA:
                        if (entrada.split(Constants.SEPARATOR).length > 1) {
                            Log.e("procesar", "No se ha podido añadir el usuario");
                        } else {
                            Log.e("procesar", "lista compartida");
                            listasViewModel.setListas(Singleton.getInstance().getListas());
                        }

                        break;
                    case Constants.CATEGORIAS_RESPUESTA_CORRECTA:
                        Log.e("procesar", entrada);
                        TreeSet<Categoria> c = QueryUtils.categoriasJson(entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().setCategorias(c);
                        categoriaViewModel.setCategorias(c);
                        break;
                    case Constants.PRODUCTOS_CATEGORIA_RESPUESTA_CORRECTA:
                        Log.e("procesar", entrada);
                        ProductosConID productosCategoria = QueryUtils.tipoProductosJson(entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().añadirNuevosProductosCategoria(productosCategoria.getId(), productosCategoria.getProductosConID());
                        modelProductos.setProductos(productosCategoria.getProductosConID());
                        break;
                    case Constants.PRODUCTOS_LISTA_CORRECTA:
                        Log.e("procesar", entrada);
                        ProductosListaConID productosLista = QueryUtils.productosLista(entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().añadirProductosLista(productosLista.getId(), productosLista.getProductosListaConID());
                        productosListaViewModel.setProductosLista(productosLista.getProductosListaConID());
                        break;
                    case Constants.LISTAS_RESPUESTA_CORRECTA:
                        Log.e("procesar", entrada);
                        Singleton.getInstance().setListas(QueryUtils.listasJson(entrada.split(Constants.SEPARATOR)[1]));
                        listasViewModel.setListas(Singleton.getInstance().getListas());

                        break;
                    case Constants.PRODUCTOS_DESPENSA_CORRECTA:
                        Log.e("procesar", entrada);
                        ProductosListaConID productosLista1 = QueryUtils.productosLista(entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().setDespensa(productosLista1.getProductosListaConID());
                        despensaViewModel.setDespensa(productosLista1.getProductosListaConID());
                        break;
                    case Constants.CREACION_NUEVA_LISTA_CORRECTA:
                        Log.e("procesar", entrada);
                        Lista l = new Lista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]), entrada.split(Constants.SEPARATOR)[2], "Administrador");
                        l.añadirUsuario(new Usuario(QueryUtils.getUsuario().getNombre(), "Administrador"));
                        Singleton.getInstance().añadirNuevaLista(l);
                        listasViewModel.añadirNuevaLista(l);
                        break;
                    case Constants.BORRAR_LISTA_ACEPTADA:
                        Log.e("procesar", entrada);

                        Singleton.getInstance().borrarLista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]));
                        listasViewModel.setListas(Singleton.getInstance().getListas());
                        break;
                    case Constants.NOTIFICACIONES_CORRECTA:
                        Log.e("procesar", entrada);
                        break;

                    case Constants.CATEGORIAS_RECETAS_CORRECTA:
                        Log.e("procesar", entrada);
                        Singleton.getInstance().setCategoriasRecetas(QueryUtils.categoriasJson(entrada.split(Constants.SEPARATOR)[1]));
                        categoriaViewModel.setCategoriasRecetas(Singleton.getInstance().getCategoriasRecetas());
                        break;
                    case Constants.RECETAS_CATEGORIA_CORRECTA:
                        RecetasConID recetasCategoria = QueryUtils.recetasJSON(entrada.split(Constants.SEPARATOR)[1]);
                        Singleton.getInstance().añadirNuevasRecetasCategoriaSeleccionada(recetasCategoria.getId(), recetasCategoria.getRecetasConID());
                        recetaViewModel.setRecetas(Singleton.getInstance().getRecetasCategoriaSeleccionada());
                        break;
                    case Constants.RECETA_ALEATORIA_CORRECTA:

                        break;
                    default:
                        Log.e("procesar", "Codigo de respuesta desconocido");
                        break;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                Log.e("error",e.getMessage());
            }
            Singleton.getInstance().peticionProcesada();
        }

        public void procesarNotificaciones(){
            for(Notificacion n:Singleton.getInstance().getNotificaciones()){
                String nombreLista="";
                for(Lista l:Singleton.getInstance().getListas()){
                    if(n.getIdLista()==l.getId())
                        nombreLista=l.getTitulo();
                }
                switch (n.getOperacion()){
                    case "add":
                        backgroundToast(getApplicationContext(),n.getAutor()+" ha añadido un productos a la lista "+nombreLista);
                        break;
                    case "delete":
                        backgroundToast(getApplicationContext(),n.getAutor()+" ha borrado un productos en la lista "+nombreLista);
                        break;
                    case "mark":
                        backgroundToast(getApplicationContext(),n.getAutor()+" ha comprado productos de la lista "+nombreLista);
                        break;
                    case "unmark":
                        backgroundToast(getApplicationContext(),n.getAutor()+" ha desmarcado productos de la lista "+nombreLista);
                        break;
                }
                if(n.getTipoNotificacion().equals("usuarios")){
                    //peticion listas
                    Singleton.getInstance().enviarPeticion(new Peticion(Constants.LISTAS_PETICION,QueryUtils.getUsuario().getId(),4));
                }else{
                    Singleton.getInstance().enviarPeticion(new Peticion(Constants.PRODUCTOS_LISTA_PETICION,QueryUtils.getUsuario().getId(),n.getIdLista()+"",5));
                    //peticion productos lista
                }
            }
        }
        public void backgroundToast(final Context context, final String msg) {
            if (context != null && msg != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.LOGOUT,QueryUtils.getUsuario().getId(),20));
        Log.e("logout","logout enviado");
    }

    @Override
    public void finish() {
        super.finish();
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.LOGOUT,QueryUtils.getUsuario().getId(),20));
        Log.e("logout","logout enviado");
    }

    public RecetaViewModel getRecetaViewModel() {
        return recetaViewModel;
    }
}

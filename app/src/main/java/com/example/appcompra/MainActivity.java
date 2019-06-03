package com.example.appcompra;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.fragment.ListasFragment;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    PeticionNuevaListaTask nuevaListaTask;
    PeticionNuevaListaTaskTest nuevaListaTaskTest;
    BorrarListaTask borrarListaTask=null;
    BorrarListaTaskTest borrarListaTaskTest=null;
    protected CompartirListaTask compartirListaTask=null;
    protected CompartirListaTaskTest compartirListaTaskTest=null;
    Lista listaSeleccionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario= (Usuario)getIntent().getExtras().getSerializable("Usuario");
        QueryUtils.setUsuario(usuario);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        botonNueva=findViewById(R.id.boton_nueva_lista);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titulo=toolbar.findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView=findViewById(R.id.header);
        TextView nombreUsuario=headView.findViewById(R.id.drawer_nombre);
        nombreUsuario.setText(usuario.getNombre());
        TextView emailUsuario=headView.findViewById(R.id.drawer_email);
        emailUsuario.setText(usuario.getEmail());
        ImageView imagenUsuario=headView.findViewById(R.id.drawer_imagen);
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
                if(position!=5){
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
                    TextView subtitle=toolbar.findViewById(R.id.subtitle);
                    subtitle.setVisibility(View.GONE);
                    LinearLayout linearToolbar=toolbar.findViewById(R.id.linearToolbar);
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

    public void updateUI(ArrayList<Lista> m){
        /*productos.clear();
        productos.addAll(m);
        */
        adapter=new ListaAdapter(m, this, R.layout.item_row_listas, this, new ListaAdapter.OnItemClickListener() {
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


    public void crearNuevaListaPopup(){
        View view = LayoutInflater.from(this).inflate(R.layout.popup_crear_lista, null);
        final AutoCompleteTextView editText=view.findViewById(R.id.crear_lista_editText);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);

        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreNuevaLista=editText.getText().toString();
                if(!nombreNuevaLista.isEmpty()){
                    //peticionNuevaLista();
                    peticionNuevaListaTest();
                    dialog.dismiss();
                }else {
                    Toast.makeText(getBaseContext(),"Introduce un nombre para la lista",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void borrarListaPopup( Lista l){
        View view = LayoutInflater.from(this).inflate(R.layout.popup_confirmacion, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        listaSeleccionada=l;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp=view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //peticionBorrarLista();
                peticionBorrarListaTest();
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
    public void compartirListaPopup( Lista l){
        View view = LayoutInflater.from(this).inflate(R.layout.popup_compartir, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AutoCompleteTextView editText=view.findViewById(R.id.editText);
        final Spinner spinnerRoles=view.findViewById(R.id.spinnerRoles);
        List<String> valoresSpinner=new ArrayList<>();
        valoresSpinner.add("Ninguno");
        valoresSpinner.add("Administrador");
        valoresSpinner.add("Participante");
        valoresSpinner.add("Espectador");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),R.layout.spinner_item,valoresSpinner
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        listaSeleccionada=l;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);
        Button botonCancelarPopUp=view.findViewById(R.id.botonCancelarPopup);
        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rolElegido=spinnerRoles.getSelectedItem().toString();
                String nombreUsuario=editText.getText().toString();
                if(!nombreUsuario.isEmpty()){
                    if(!rolElegido.equals("Ninguno") && !nombreUsuario.isEmpty()) {
                        peticionCompartirListaTest(nombreUsuario,rolElegido);
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getBaseContext(),"Elige un rol para el usuario",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"Introduce algún nombre",Toast.LENGTH_LONG).show();
                }


            }
        });
        botonCancelarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void peticionNuevaLista(){
        nuevaListaTask=new PeticionNuevaListaTask();
        nuevaListaTask.execute((Void) null);
    }
    public void peticionNuevaListaTest(){
        nuevaListaTaskTest=new PeticionNuevaListaTaskTest();
        nuevaListaTaskTest.execute((Void) null);
    }
    public void peticionBorrarLista(){
        borrarListaTask=new BorrarListaTask();
        borrarListaTask.execute((Void) null);
    }
    public void peticionBorrarListaTest(){
        borrarListaTaskTest=new BorrarListaTaskTest();
        borrarListaTaskTest.execute((Void) null);
    }
    public void peticionCompartirLista(String usuario,String rol){
        compartirListaTask=new CompartirListaTask(usuario,rol);
        compartirListaTask.execute((Void) null);
    }
    public void peticionCompartirListaTest(String usuario,String rol){
        compartirListaTaskTest=new CompartirListaTaskTest(usuario,rol);
        compartirListaTaskTest.execute((Void) null);
    }

    public class PeticionNuevaListaTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String json;
        private Lista lista;
        private Boolean listaCreada;

        PeticionNuevaListaTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.CREACION_NUEVA_LISTA+Constants.SEPARATOR+usuario.getId()+Constants.SEPARATOR+nombreNuevaLista+Constants.SEPARATOR);
                String entrada=in.readLine();
                Log.e("respuesta",entrada.split(Constants.SEPARATOR)[1]);
                if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.CREACION_NUEVA_LISTA_CORRECTA)){
                    json=entrada.split(Constants.SEPARATOR)[1];
                    lista=new Lista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]),nombreNuevaLista,"Administrador");
                    Singleton.getInstance().añadirNuevaLista(lista);
                }else{
                    listaCreada=false;
                }

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            return listaCreada;
        }

        @Override
        protected void onPostExecute(final Boolean creada) {
            if(creada)
                actualizarListas();
            else{
                Toast.makeText(getBaseContext(), "No se ha podido crear la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class PeticionNuevaListaTaskTest extends AsyncTask<Void, Void, Boolean> {
        private String json;
        private Lista lista;
        private Boolean listaCreada;

        PeticionNuevaListaTaskTest() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String entrada=Constants.DUMMY_LISTA_ACEPTADA;
            lista=new Lista(Integer.parseInt(entrada.split(Constants.SEPARATOR)[1]),nombreNuevaLista,"Administrador");
            Singleton.getInstance().añadirNuevaLista(lista);
            listaCreada=true;
            return listaCreada;
        }

        @Override
        protected void onPostExecute(final Boolean creada) {
            if(creada)
                updateUI(Singleton.getInstance().getListas());

            else{
                Toast.makeText(getBaseContext(), "No se ha podido crear la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }


    }
    public class BorrarListaTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private boolean peticion;

        BorrarListaTask() {
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            socket= QueryUtils.getSocket();
            try {
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.BORRAR_LISTA_PETICION);
                Singleton.getInstance().borrarLista(listaSeleccionada);

            } catch (IOException e) {
                Log.e("errorIO",e.getMessage());
            }

            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean creada) {
            if(creada)
                actualizarListas();
            else{
                Toast.makeText(getBaseContext(), "No se ha podido borrar la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    public class BorrarListaTaskTest extends AsyncTask<Void, Void, Boolean> {

        private boolean peticion;

        BorrarListaTaskTest() {
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Singleton.getInstance().borrarLista(listaSeleccionada);
            Cambios.getInstance().addCambioLS(listaSeleccionada.getId());
            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean borrada) {
            if(borrada)
                actualizarListas();
            else{
                Toast.makeText(getBaseContext(), "No se ha podido borrar la lista", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    public class CompartirListaTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private boolean peticion;
        private String usuario;
        private String rol;
        CompartirListaTask(String usuario,String rol)
        {
            this.usuario=usuario;
            this.rol=rol;
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            socket = QueryUtils.getSocket();
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String salida = Constants.COMPARTIR_LISTA_PETICION + Constants.SEPARATOR + QueryUtils.getUsuario().getId() + Constants.SEPARATOR + listaSeleccionada.getId() + Constants.SEPARATOR + usuario + Constants.SEPARATOR + rol;
                out.println(salida);
                String entrada = in.readLine();
                if (entrada.split(Constants.SEPARATOR)[1] != null) {
                    if (entrada.split(Constants.SEPARATOR)[1].equals(Constants.COMPARTIR_LISTA_CORRECTA)) {
                        peticion = true;
                    } else {
                        peticion = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean aceptada) {
            if(aceptada){
                listaSeleccionada.añadirUsuario(new Usuario(usuario,rol));
            }else{
                Toast.makeText(getBaseContext(), "No existe ese usuario", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    public class CompartirListaTaskTest extends AsyncTask<Void, Void, Boolean> {

        private boolean peticion;
        private String usuario;
        private String rol;
        CompartirListaTaskTest(String usuario,String rol)
        {
            this.usuario=usuario;
            this.rol=rol;
            peticion=true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String salida=Constants.COMPARTIR_LISTA_PETICION+Constants.SEPARATOR+QueryUtils.getUsuario().getId()+Constants.SEPARATOR+listaSeleccionada.getId()+Constants.SEPARATOR+usuario+Constants.SEPARATOR+rol;
            String entrada=Constants.DUMMY_COMPARTIR_LISTA_ACEPTADA;
            if(entrada!=null) {
                if(entrada.equals(Constants.COMPARTIR_LISTA_CORRECTA)){
                    peticion=true;
                }else{
                    peticion=false;
                }
            }

            return peticion;
        }

        @Override
        protected void onPostExecute(final Boolean aceptada) {
            if(aceptada){
                actualizarListas();
                listaSeleccionada.añadirUsuario(new Usuario(usuario,rol));
            }else{
                Toast.makeText(getBaseContext(), "No existe ese usuario", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    public void actualizarListas(){
        updateUI(Singleton.getInstance().getListas());
        ListasFragment page = (ListasFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" +1);
        page.updateUI(Singleton.getInstance().getListas());
    }
}

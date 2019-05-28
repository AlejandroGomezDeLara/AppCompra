package com.example.appcompra;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appcompra.adapters.ListaAdapter;
import com.example.appcompra.adapters.MenuAdapter;
import com.example.appcompra.clases.Lista;
import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.Cambios;
import com.example.appcompra.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView menu;
    RecyclerView recyclerView;
    ListaAdapter adapter;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario= (Usuario)getIntent().getExtras().getSerializable("Usuario");
        QueryUtils.setUsuario(usuario);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                            break;
                        case R.id.menu_listas:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.menu_home:
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.menu_productos:
                            viewPager.setCurrentItem(3);
                            break;
                        case R.id.menu_recetas:
                            viewPager.setCurrentItem(4);

                            break;
                    }
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
            public void onItemClick(Lista l) {

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
}

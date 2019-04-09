package com.example.appcompra;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.appcompra.adapters.MenuAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView menu;
    LinearLayout linearFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Principal");
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        MenuAdapter adapter=new MenuAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //menu de abajo
        menu=findViewById(R.id.menu_bottom);
        menu.setOnNavigationItemSelectedListener(navListener);
        menu.getMenu().getItem(1).setChecked(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    menu.getMenu().getItem(0).setChecked(false);
                }
                menu.getMenu().getItem(position).setChecked(true);
                prevMenuItem = menu.getMenu().getItem(position);

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setCurrentItem(1);
    }


    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_despensa:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.menu_home:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.menu_listas:
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

}

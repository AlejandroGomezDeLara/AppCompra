package com.example.appcompra.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.appcompra.R;
import com.example.appcompra.adapters.MenuAdapter;

public class ProductosFragment extends Fragment {
    ViewPager viewPager;
    MenuItem prevMenuItem;
    BottomNavigationView menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        MenuAdapter adapter = new MenuAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //menu de abajo
        menu = getActivity().findViewById(R.id.menu_bottom);
        menu.setOnNavigationItemSelectedListener(navListener);
        menu.getMenu().getItem(4).setChecked(true);

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
                menu.getMenu().getItem(position).setChecked(true);
                prevMenuItem = menu.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setCurrentItem(1);
        return view;
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

    public void onResume(){
        super.onResume();
    }
}

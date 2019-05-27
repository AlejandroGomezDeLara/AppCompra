package com.example.appcompra.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.appcompra.fragment.DespensaFragment;
import com.example.appcompra.fragment.ListasFragment;
import com.example.appcompra.fragment.PrincipalFragment;
import com.example.appcompra.fragment.ProductosFragment;
import com.example.appcompra.fragment.RecetasFragment;

public class MenuAdapter extends FragmentPagerAdapter {
    final DespensaFragment despensaFragment=new DespensaFragment();
    final ListasFragment listasFragment=new ListasFragment();
    final PrincipalFragment principalFragment=new PrincipalFragment();
    final ProductosFragment productosFragment=new ProductosFragment();
    final RecetasFragment recetasFragment=new RecetasFragment();

    public MenuAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return despensaFragment;
            case 1: return listasFragment;
            case 2: return principalFragment;
            case 3: return productosFragment;
            case 4: return recetasFragment;
        }
        return principalFragment;
    }
    @Override
    public int getCount() {
        return 5;
    }
}

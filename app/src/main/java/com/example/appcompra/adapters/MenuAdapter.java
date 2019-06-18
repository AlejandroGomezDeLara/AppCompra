package com.example.appcompra.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.appcompra.fragment.DespensaFragment;
import com.example.appcompra.fragment.InteriorListaFragment;
import com.example.appcompra.fragment.InteriorRecetaFragment;
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
    final InteriorListaFragment interiorListaFragment=new InteriorListaFragment();
    final InteriorRecetaFragment interiorRecetaFragment=new InteriorRecetaFragment();

    public MenuAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = principalFragment;
        switch (i){
            case 0:
                fragment=despensaFragment;
            break;
            case 1:
                fragment=listasFragment;
            break;
            case 2:
                fragment=principalFragment;
            break;
            case 3:
                fragment=productosFragment;
            break;
            case 4:
                fragment=recetasFragment;
                break;
            case 5:
                fragment=interiorListaFragment;
                break;
            case 6:
                fragment=interiorRecetaFragment;
                break;
        }
        return fragment;
    }
    @Override
    public int getCount() {
        return 7;
    }

}

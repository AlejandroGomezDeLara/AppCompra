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
    public MenuAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return new DespensaFragment();
            case 1: return new PrincipalFragment();
            case 2: return new ListasFragment();
            case 3: return new ProductosFragment();
            case 4: return new RecetasFragment();
        }
        return new PrincipalFragment();
    }
    @Override
    public int getCount() {
        return 5;
    }
}

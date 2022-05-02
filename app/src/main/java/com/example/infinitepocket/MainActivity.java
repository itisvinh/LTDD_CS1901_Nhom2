package com.example.infinitepocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.infinitepocket.fragments.FragmentFactory;
import com.example.infinitepocket.utilities.CustomizedToast;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar bottomMenu;
    boolean doubleBackToExitPressedOnce = false;

    // PRESS THE BACK BUTTON TWICE TO EXIT
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        CustomizedToast.show(this, "Tap BACK again to exit");
        new Handler(Looper.getMainLooper()).postDelayed( () -> {
            doubleBackToExitPressedOnce=false;
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        replaceFragment(FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT));

        ini();
        setListeners();
        bottomMenu.setItemSelected(R.id.menu_item_wallet, true);

    }

    // mapping UI components
    private void ini() {
        bottomMenu = findViewById(R.id.bottomMenu);
    }

    // handling listeners of UI components
    private void setListeners() {
        bottomMenu.setOnItemSelectedListener( id -> {
            Fragment fragment = null;

            switch (id) {
                case R.id.menu_item_wallet:
                    fragment = FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT);
                    getWindow().setStatusBarColor(Color.parseColor("#8E0640"));
                    break;
                case R.id.menu_item_about:
                    fragment = FragmentFactory.get(FragmentFactory.ABOUT_FRAGMENT);
                    getWindow().setStatusBarColor(Color.parseColor("#234C09"));
                    break;
            }
            if (fragment != null)
                replaceFragment(fragment);

        });

        getFragmentManager().addOnBackStackChangedListener( () -> {

        });

    }

    private void replaceFragment(Fragment fragment) {
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_frame, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            //ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

}
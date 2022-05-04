package com.example.infinitepocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.RelativeLayout;

import com.example.infinitepocket.fragments.FragmentFactory;
import com.example.infinitepocket.fragments.TopFrameFragment;
import com.example.infinitepocket.fragments.tools.FragmentReplacer;
import com.example.infinitepocket.utilities.CustomizedToast;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar bottomMenu;
    boolean doubleBackToExitPressedOnce = false;
    boolean topFrameChangeRequired = true;

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
        //replaceFragment(R.id.main_frame, FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT));

        ini();
        setListeners();
        bottomMenu.setItemSelected(R.id.menu_item_wallet, true);

    }



    // mapping UI components
    private void ini() {
        bottomMenu = findViewById(R.id.bottomMenu);
    }

    private void changeTopFrame() {
        if (topFrameChangeRequired) {
            Fragment topFrameFragment = FragmentFactory.get(FragmentFactory.TOP_FRAME_FRAGMENT);
            FragmentReplacer.replace(this, R.id.main_frame, topFrameFragment);
        }
        topFrameChangeRequired = false;
    }

    // handling listeners of UI components
    private void setListeners() {
        bottomMenu.setOnItemSelectedListener( id -> {

            switch (id) {
                case R.id.menu_item_wallet:
                    changeTopFrame();
                    Fragment walletFragment = FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT);
                    FragmentReplacer.replace(this, R.id.bottom_container, walletFragment);
                    break;

                case R.id.menu_item_reports:
                    changeTopFrame();
                    Fragment reportsFragment = FragmentFactory.get(FragmentFactory.REPORTS_FRAGMENT);
                    FragmentReplacer.replace(this, R.id.bottom_container, reportsFragment);
                    break;

                case R.id.menu_item_planning:
                    changeTopFrame();
                    Fragment planningFragment = FragmentFactory.get(FragmentFactory.PLANNING_FRAGMENT);
                    FragmentReplacer.replace(this, R.id.bottom_container, planningFragment);
                    break;

                case R.id.menu_item_about:
                    Fragment aboutFragment = FragmentFactory.get(FragmentFactory.ABOUT_FRAGMENT);
                    FragmentReplacer.replace(this, R.id.main_frame, aboutFragment);
                    topFrameChangeRequired = true;
                    getWindow().setStatusBarColor(Color.parseColor("#234C09"));
                    break;
            }
//            if (fragment != null)
//                replaceFragment(R.id.main_frame, fragment);

        });

        getFragmentManager().addOnBackStackChangedListener( () -> {

        });
    }


}
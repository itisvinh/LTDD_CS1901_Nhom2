package com.example.infinitepocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.infinitepocket.database.DatabaseHelper;
import com.example.infinitepocket.fragments.FragmentFactory;
import com.example.infinitepocket.fragments.tools.FragmentHelper;
import com.example.infinitepocket.modelobjects.Wallet;
import com.example.infinitepocket.utilities.CustomizedToast;
import com.example.infinitepocket.viewmodels.MainViewModel;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    ChipNavigationBar bottomMenu;
    boolean doubleBackToExitPressedOnce = false;
    boolean topFrameChangeRequired = true;
    Fragment prevBottomFragment = null;

    Communicator communicator = Communicator.getInstance();

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

    private void startCreateWalletActivity() {
//        int prevWallegtId = new DatabaseHelper(getApplicationContext()).getPreviousWalletId();
//        Wallet wallet = new DatabaseHelper(getApplicationContext()).getWalletFromId(prevWallegtId);
//
//        if (wallet != null) {
//            communicator.setCurrentWallet(wallet);
//            return;
//        }
        Intent myIntent = new Intent(this, CreateWalletActivity.class);
        // myIntent.putExtra("key", value); //Optional parameters
        startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        communicator.addOnSetCurrentWalletObserver( wallet -> {
            CustomizedToast.show(this, "showing new wallet: " + wallet.getName());
        });

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //replaceFragment(R.id.main_frame, FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT));
        addingFragments();
        prevBottomFragment = FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT);
        startCreateWalletActivity();
        ini();
        setListeners();
        bottomMenu.setItemSelected(R.id.menu_item_wallet, true);
    }


    // mapping UI components
    private void ini() {
        bottomMenu = findViewById(R.id.bottomMenu);
    }

    private void addingFragments() {
        FragmentHelper.add(this, R.id.main_frame, FragmentFactory.get(FragmentFactory.ABOUT_FRAGMENT));
        FragmentHelper.add(this, R.id.main_frame, FragmentFactory.get(FragmentFactory.TOP_FRAME_FRAGMENT));
        FragmentHelper.add(this, R.id.bottom_container, FragmentFactory.get(FragmentFactory.WALLET_FRAGMENT));
        FragmentHelper.add(this, R.id.bottom_container, FragmentFactory.get(FragmentFactory.REPORTS_FRAGMENT));
        //FragmentHelper.add(this, R.id.bottom_container, FragmentFactory.get(FragmentFactory.PLANNING_FRAGMENT));
    }

    private void changeTopFrame() {
        if (topFrameChangeRequired) {
            Fragment aboutFragment = FragmentFactory.get(FragmentFactory.ABOUT_FRAGMENT);
            FragmentHelper.hide(this, aboutFragment);
            Fragment topFrameFragment = FragmentFactory.get(FragmentFactory.TOP_FRAME_FRAGMENT);
            //FragmentHelper.replace(this, R.id.main_frame, topFrameFragment);
            FragmentHelper.show(this, topFrameFragment);
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
                    if (prevBottomFragment != walletFragment) {
                        FragmentHelper.hide(this, prevBottomFragment);
                        prevBottomFragment = walletFragment;
                    }
                    FragmentHelper.show(this, walletFragment);
                    break;

                case R.id.menu_item_reports:
                    changeTopFrame();
                    Fragment reportsFragment = FragmentFactory.get(FragmentFactory.REPORTS_FRAGMENT);
                    if (prevBottomFragment != reportsFragment) {
                        FragmentHelper.hide(this, prevBottomFragment);
                        prevBottomFragment = reportsFragment;
                    }
                    FragmentHelper.show(this, reportsFragment);
                    break;

//                case R.id.menu_item_planning:
//                    changeTopFrame();
//                    Fragment planningFragment = FragmentFactory.get(FragmentFactory.PLANNING_FRAGMENT);
//                    if (prevBottomFragment != planningFragment) {
//                        FragmentHelper.hide(this, prevBottomFragment);
//                        prevBottomFragment = planningFragment;
//                    }
//                    FragmentHelper.show(this, planningFragment);
//                    break;

                case R.id.menu_item_about:
                    Fragment aboutFragment = FragmentFactory.get(FragmentFactory.ABOUT_FRAGMENT);
                    Fragment topFrameFragment = FragmentFactory.get(FragmentFactory.TOP_FRAME_FRAGMENT);
                    FragmentHelper.hide(this, prevBottomFragment);
                    FragmentHelper.hide(this, topFrameFragment);
                    FragmentHelper.show(this, aboutFragment);
                    topFrameChangeRequired = true;
                    getWindow().setStatusBarColor(Color.parseColor("#234C09"));
                    break;
            }


        });

    }


}
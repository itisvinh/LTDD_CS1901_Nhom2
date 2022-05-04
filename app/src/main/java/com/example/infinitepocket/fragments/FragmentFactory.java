package com.example.infinitepocket.fragments;

import androidx.fragment.app.Fragment;

// SINGLETON CLASS
// HANDLING INITIALIZATION OF PREDEFINED FRAGMENTS
public class FragmentFactory {
    private static AboutFragment aboutFragment;
    private static WalletFragment walletFragment;
    private static TopFrameFragment topFrameFragment;
    private static PlanningFragment planningFragment;
    private static ReportsFragment reportsFragment;

    // FRAGMENT IDs
    public static final int WALLET_FRAGMENT = 0;
    public static final int ABOUT_FRAGMENT = 1;
    public static final int TOP_FRAME_FRAGMENT = 2;
    public static final int PLANNING_FRAGMENT = 3;
    public static final int REPORTS_FRAGMENT = 4;


    private FragmentFactory() {}

    private static boolean exists(Fragment fragment) {
        return fragment != null ? true : false;
    }

    // RETURN A FRAGMENT AND INITIALIZE IT ONLY ONCE
    public static Fragment get(int fragment_name) {


        switch (fragment_name) {
            case WALLET_FRAGMENT:
                if (!exists(walletFragment))
                    walletFragment = new WalletFragment();
                return walletFragment;

            case ABOUT_FRAGMENT:
                if (!exists(aboutFragment))
                    aboutFragment = new AboutFragment();
                return aboutFragment;

            case TOP_FRAME_FRAGMENT:
                if (!exists(topFrameFragment))
                    topFrameFragment = new TopFrameFragment();
                return topFrameFragment;

            case PLANNING_FRAGMENT:
                if (!exists(planningFragment))
                    planningFragment = new PlanningFragment();
                return planningFragment;

            case REPORTS_FRAGMENT:
                if (!exists(reportsFragment))
                    reportsFragment = new ReportsFragment();
                return reportsFragment;
        }
        return null;

    }

}

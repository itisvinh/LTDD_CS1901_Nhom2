package com.example.infinitepocket.fragments;

import androidx.fragment.app.Fragment;

// SINGLETON CLASS
// HANDLING INITIALIZATION OF PREDEFINED FRAGMENTS
public class FragmentFactory {
    private static AboutFragment aboutFragment;
    private static WalletFragment walletFragment;

    // FRAGMENT IDs
    public static final int WALLET_FRAGMENT = 0;
    public static final int ABOUT_FRAGMENT = 1;

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
        }
        return null;

    }

}

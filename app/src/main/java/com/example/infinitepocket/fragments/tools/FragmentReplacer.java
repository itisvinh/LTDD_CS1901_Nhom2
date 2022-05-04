package com.example.infinitepocket.fragments.tools;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

// Replacing fragment of a view
// Args:
//      context: current activity
//      containerId: view ID
//      fragment: fragment to change to
//      addedToBackstack: true if adding this fragment to the backstack
public final class FragmentReplacer {
    private FragmentReplacer() {
    }

    public static void replace(AppCompatActivity context, int containerId, Fragment fragment, boolean addedToBackstack) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = context.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(containerId, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addedToBackstack)
                ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public static void replace(AppCompatActivity context, int containerId, Fragment fragment) {
        replace(context, containerId, fragment, false);
    }
}

package com.example.infinitepocket.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.infinitepocket.AddTransactionActivity;
import com.example.infinitepocket.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WalletFragment extends Fragment {
    LinearLayout root;
    FloatingActionButton add_transaction;

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ini(view);
        setListeners();
        View child = getLayoutInflater().inflate(R.layout.transaction_item, null);
        root.addView(child);
    }

    private void ini(View view) {
        root = view.findViewById(R.id.frag_wallet_root);
        add_transaction = view.findViewById(R.id.fab_add_trans);
    }

    private void setListeners() {
        add_transaction.setOnClickListener( view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddTransactionActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.infinitepocket.fragments;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.example.infinitepocket.AddTransactionActivity;
import com.example.infinitepocket.Communicator;
import com.example.infinitepocket.R;
import com.example.infinitepocket.TransactionAddedRole;
import com.example.infinitepocket.database.DatabaseHelper;
import com.example.infinitepocket.modelobjects.Category;
import com.example.infinitepocket.modelobjects.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalletFragment extends Fragment {
    LinearLayout root;
    FloatingActionButton add_transaction;
    Communicator communicator = Communicator.getInstance();

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
        setCommunicatorParams();
    }

    private void setCommunicatorParams() {
        communicator.addOnCreatedTransactionObservables( transaction -> {
            addNewTransactionCard(transaction);
        });
    }

    private void addNewTransactionCard(Transaction transaction) {
        View child = getLayoutInflater().inflate(R.layout.transaction_item, null);
        CircleImageView civ = child.findViewById(R.id.trans_item_icon);
        TextView cat = child.findViewById(R.id.trans_item_category);
        TextView details = child.findViewById(R.id.trans_item_details);
        TextView amount = child.findViewById(R.id.trans_item_amount);
        TextView id = child.findViewById(R.id.trans_item_id);

        civ.setImageDrawable(getResources().getDrawable(transaction.getCategory().getIconId()));
        cat.setText(transaction.getCategory().getFormattedName());
        details.setText(transaction.getNote());

        if (transaction.getCategory().getId() != Category.INCOME) {
            amount.setText("-" + String.valueOf(transaction.getAmount()));
            amount.setTextColor(Color.rgb(200,10,10));
        }
        else {
            amount.setText("+" + String.valueOf(transaction.getAmount()));
            amount.setTextColor(Color.rgb(10,200,10));
        }
        id.setText(String.valueOf(transaction.getId()));

        // listeners
        child.setOnClickListener( view -> {
            int transId = Integer.parseInt(((TextView) view.findViewById(R.id.trans_item_id)).getText().toString());
            Transaction tmpTrans = new DatabaseHelper(getActivity().getApplicationContext()).getTransactionById(transId);

            if (tmpTrans != null) {
                communicator.setLastTransactionNoFire(tmpTrans);
                communicator.setTransactionAddedRole(TransactionAddedRole.ROLE_EDIT);
                Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
                startActivity(intent);
            }
        });

        root.addView(child);
    }


    private void ini(View view) {
        root = view.findViewById(R.id.frag_wallet_root);
        add_transaction = view.findViewById(R.id.fab_add_trans);
    }

    private void setListeners() {
        add_transaction.setOnClickListener( view -> {
            communicator.setTransactionAddedRole(TransactionAddedRole.ROLE_CREATE);
            Intent intent = new Intent(getActivity().getApplicationContext(), AddTransactionActivity.class);
            startActivity(intent);
        });
    }
}
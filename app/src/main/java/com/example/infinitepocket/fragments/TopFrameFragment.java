package com.example.infinitepocket.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinitepocket.Communicator;
import com.example.infinitepocket.CreateWalletActivity;
import com.example.infinitepocket.CreateWalletMode;
import com.example.infinitepocket.MainActivity;
import com.example.infinitepocket.R;
import com.example.infinitepocket.modelobjects.Category;
import com.example.infinitepocket.viewmodels.MainViewModel;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TopFrameFragment extends Fragment {
    Button datePicker;
    Button displayAll;
    TextView wallet_name;
    TextView wallet_balance;
    TextView monetary_unit_1;
    TextView monetary_unit_2;
    TextView wallet_spending;
    TextView wallet_available;
    View edit_wallet;
    int currentButtonId;

    Communicator communicator = Communicator.getInstance();


    public TopFrameFragment() {
        // Required empty public constructor
    }

    // SAVING FRAGMENT STATE
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void ini(View view) {
        datePicker = view.findViewById(R.id.date_picker);
        displayAll = view.findViewById(R.id.display_all);
        wallet_name = view.findViewById(R.id.tv_wallet_name);
        wallet_balance = view.findViewById(R.id.tv_balance);
        monetary_unit_1 = view.findViewById(R.id.tv_monetary_unit_1);
        monetary_unit_2 = view.findViewById(R.id.tv_monetary_unit_2);
        edit_wallet = view.findViewById(R.id.edit_wallet);
        wallet_spending = view.findViewById(R.id.tv_spending);
        wallet_available = view.findViewById(R.id.tv_available);
    }

    private void setListeners() {
        datePicker.setOnClickListener(this::buttonSelected);
        displayAll.setOnClickListener(this::buttonSelected);
        edit_wallet.setOnClickListener( view -> {
            communicator.setCreateWalletMode(CreateWalletMode.MODE_EDIT);
            Intent intent = new Intent(getActivity(), CreateWalletActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        communicator.addOnSetCurrentWalletObserver( wallet -> {
            wallet_name.setText(wallet.getName());
            wallet_balance.setText(String.valueOf(wallet.getBalance()));
            String symbol = wallet.getCurrency().getSymbol();
            monetary_unit_1.setText(symbol);
            monetary_unit_2.setText(symbol);
            wallet_spending.setText(String.valueOf(wallet.getUsed()));
            wallet_available.setText(String.valueOf(wallet.getAvailable()));
        });
        communicator.addOnChangedCurrentWalletObserver( wallet -> {
            wallet_name.setText(wallet.getName());
            wallet_balance.setText(String.valueOf(wallet.getBalance()));
            wallet_spending.setText(String.valueOf(wallet.getUsed()));
            wallet_available.setText(String.valueOf(wallet.getAvailable()));
        });
        ini(view);
        setListeners();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_frame, container, false);
    }


    private boolean updateBackground(Button button, int backgroundId) {
        if (button != null) {
            button.setBackground(AppCompatResources.getDrawable(getActivity().getApplicationContext(), backgroundId));
            return true;
        }
        return false;
    }

    private void buttonSelected(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.date_picker:
                if (id != currentButtonId) {
                    currentButtonId = id;
                    updateBackground(datePicker, R.drawable.date_picker_selected);
                    datePicker.setTextColor(Color.WHITE);
                    updateBackground(displayAll, R.drawable.date_picker);
                    displayAll.setTextColor(Color.BLACK);
                    datePicker.setText("updated");
                }
                    startAnimation(datePicker);
                    displayPopupDatePicker();
                break;
            case R.id.display_all:
                if (id != currentButtonId) {
                    currentButtonId = id;
                    updateBackground(datePicker, R.drawable.date_picker);
                    datePicker.setTextColor(Color.BLACK);
                    updateBackground(displayAll, R.drawable.date_picker_selected);
                    displayAll.setTextColor(Color.WHITE);
                }
                startAnimation(displayAll);
                break;
        }
    }

    private void displayPopupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                //updateLabel();
            }
        };

        new DatePickerDialog(getActivity()
                , dateSetListener
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void startAnimation(Button button) {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.button_tapped_anim);
        button.startAnimation(animation);
    }


}
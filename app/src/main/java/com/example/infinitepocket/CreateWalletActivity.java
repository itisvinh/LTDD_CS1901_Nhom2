package com.example.infinitepocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.infinitepocket.adapters.DropDownMenuAdapter;
import com.example.infinitepocket.database.DatabaseHelper;
import com.example.infinitepocket.items.DropDownItem;
import com.example.infinitepocket.modelobjects.Currency;
import com.example.infinitepocket.modelobjects.Wallet;
import com.example.infinitepocket.utilities.CustomizedToast;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateWalletActivity extends AppCompatActivity {
    View back_button_wallet;
    TextView create_wallet;
    AutoCompleteTextView currency_selector;
    TextInputLayout currency_selector_layout;
    EditText wallet_name;
    EditText wallet_balance;

    DropDownMenuAdapter currency_adapter;
    int selected_currency_position = -1;

    Communicator communicator = Communicator.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        getSupportActionBar().hide();
        ini();
        setListeners();
        checkMode();
        loadCurrencies();
    }

    private void checkMode() {
        if (communicator.getCreateWalletMode() == CreateWalletMode.MODE_EDIT) {
            Wallet wallet = communicator.getCurrentWallet();
            wallet_name.setText(wallet.getName());
            wallet_balance.setText(String.valueOf(wallet.getBalance()));
            currency_selector.setText(wallet.getCurrency().getName());
            currency_selector_layout.setStartIconDrawable(wallet.getCurrency().getDrawableIconId());
            currency_selector.setEnabled(false);
            create_wallet.setText("EDIT");
            selected_currency_position = wallet.getCurrency().getId();
        }
    }

    private void ini() {
        back_button_wallet = findViewById(R.id.back_button_wallet);
        create_wallet = findViewById(R.id.create_wallet);
        currency_selector = findViewById(R.id.currency_selector);
        currency_selector_layout = findViewById(R.id.currency_selector_layout);
        wallet_name = findViewById(R.id.wallet_name);
        wallet_balance = findViewById(R.id.wallet_balance);
    }

    private void setListeners() {
        back_button_wallet.setOnClickListener( view -> {
            this.finish();
        });
        currency_selector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int res_id = currency_adapter.getItem(position).getIconId();
                currency_selector_layout.setStartIconDrawable(res_id);
                selected_currency_position = position;
            }
        });

        create_wallet.setOnClickListener( view -> {
            Wallet wallet = tryCreatingWallet();
            if (wallet != null) {
                if (communicator.getCreateWalletMode() == CreateWalletMode.MODE_CREATE) {
                    boolean res = new DatabaseHelper(getApplicationContext()).addNewWallet(wallet);
                    if (res) {
                        communicator.setCurrentWallet(wallet);
                        //new DatabaseHelper(getApplicationContext()).updatePreviousWalletId(wallet.getId());
                        CustomizedToast.show(this, "New wallet is created");
                    } else
                        CustomizedToast.show(this, "Failed to create new wallet");
                }
                else if (communicator.getCreateWalletMode() == CreateWalletMode.MODE_EDIT) {
                    Wallet currentWallet = communicator.getCurrentWallet();
                    Wallet cpWallet = currentWallet.clone();

                    boolean res = new DatabaseHelper(getApplicationContext()).updateWallet(cpWallet);
                    if (res) {
                        currentWallet.beginEdit().setName(wallet.getName()).commitEdit();
                        //communicator.setCreateWalletMode(CreateWalletMode.MODE_CREATE);
                        CustomizedToast.show(this, "Wallet has been updated");
                    } else
                        CustomizedToast.show(this, "Failed to update wallet");
                }
                this.finish();
            }
        });
    }

    private String tryGettingName() {
        if (wallet_name.getText().toString().length() < 3) {
            CustomizedToast.show(this, "Wallet name must be at least 3 characters");
            return null;
        } else
            return wallet_name.getText().toString();
    }

    private Currency tryGettingCurrency() {
        if (selected_currency_position == -1) {
            CustomizedToast.show(this, "Select currency first");
            return null;
        } else {
            String sel_item_name = currency_adapter.getItem(selected_currency_position).getItemName().trim();
            return new Currency(Currency.getCurrencyIdByName(sel_item_name));
        }
    }

    private Double tryGettingBalance() {
        if (wallet_balance.getText().toString().equals("")) {
            CustomizedToast.show(this, "Enter your balance");
            return null;
        } else {
            return Double.parseDouble(wallet_balance.getText().toString());
        }
    }

    private Wallet tryCreatingWallet() {
        String wName = tryGettingName();
        Currency wCurrency = tryGettingCurrency();
        Double wBalance = tryGettingBalance();

        if (wName == null || wCurrency == null || wBalance == null)
            return null;

        return new Wallet(wName, wCurrency, wBalance);
    }



    private void loadCurrencies() {
        List<DropDownItem> items = new ArrayList<>(Currency.MAX_ID);
        for (int i = 0; i < Currency.MAX_ID; i++) {
            Currency currency = new Currency(i);
            DropDownItem item = new DropDownItem(currency.getName(), currency.getDrawableIconId());
            items.add(item);
        }

        currency_adapter = new DropDownMenuAdapter(CreateWalletActivity.this, items);
        currency_selector.setAdapter(currency_adapter);
    }
}
package com.example.infinitepocket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.infinitepocket.adapters.DropDownMenuAdapter;
import com.example.infinitepocket.database.DatabaseHelper;
import com.example.infinitepocket.interfaces.Notifiable;
import com.example.infinitepocket.items.DropDownItem;
import com.example.infinitepocket.modelobjects.Category;
import com.example.infinitepocket.modelobjects.Event;
import com.example.infinitepocket.modelobjects.Transaction;
import com.example.infinitepocket.utilities.CustomizedToast;
import com.example.infinitepocket.utilities.SimpleDateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {
    View back_transaction;
    TextView create_transaction;
    EditText transaction_date;
    AutoCompleteTextView transaction_category_selector;
    //AutoCompleteTextView transaction_event_selector;
    EditText transaction_price;
    EditText transaction_details;

    Calendar globalCalendar = Calendar.getInstance();
    Communicator communicator = Communicator.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getSupportActionBar().hide();
        ini();
        setListeners();
        checkMode();
        iniViews();
    }

    private void checkMode() {
        if (communicator.getTransactionAddedRole() == TransactionAddedRole.ROLE_EDIT) {
            create_transaction.setText("EDIT");
            Transaction transaction = communicator.getLastTransaction();
            transaction_category_selector.setText(transaction.getCategory().getFormattedName());
            // set cat icon here
            transaction_date.setText(SimpleDateHelper.simpleDateFormat(transaction.getDate()));
            transaction_price.setText(String.valueOf(transaction.getAmount()));
            transaction_details.setText(transaction.getNote());
        }
    }

    public AddTransactionActivity() {
        super();
    }

    private void ini() {
        back_transaction = findViewById(R.id.back_button_trans);
        create_transaction = findViewById(R.id.create_trans);
        transaction_date = findViewById(R.id.trans_date);
        transaction_category_selector = findViewById(R.id.trans_category_selector);
        //transaction_event_selector = findViewById(R.id.trans_event_selector);
        transaction_price = findViewById(R.id.trans_price);
        transaction_details = findViewById(R.id.trans_details);
    }

    private void iniViews() {
        updateSelectedDate();

        // ini category
        transaction_category_selector.setText(Category.getFormattedName(Category.FOOD));

        // category selector
        List<DropDownItem> items = new ArrayList<>(Category.MAX_ID);
        for (int i = 0; i <= Category.MAX_ID; i++)
            items.add(new DropDownItem(Category.getFormattedName(i), Category.getIconId(i)));

        DropDownMenuAdapter cat_adapter = new DropDownMenuAdapter(this, items);
        transaction_category_selector.setAdapter(cat_adapter);

//        // event selector
//        transaction_event_selector.setText("None");
//        // event selector
//        List<DropDownItem> event_items = new ArrayList<>(10);
//        event_items.add(new DropDownItem("None", R.drawable.ic_menu_wallet));
//
//        DropDownMenuAdapter event_adapter = new DropDownMenuAdapter(this, event_items);
//        transaction_event_selector.setAdapter(event_adapter);

    }

    @Deprecated
    private String formatAsString(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
    }

    private void setListeners() {
        back_transaction.setOnClickListener( view -> {
            finish();
        });
        transaction_date.setOnTouchListener( (view , event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                displayPopupDatePicker();
                updateSelectedDate();
            }
            return false;
        });

        create_transaction.setOnClickListener( view -> {
            Transaction transaction = tryCreatingTransaction();

            if (transaction != null) {
                if (communicator.getTransactionAddedRole() == TransactionAddedRole.ROLE_CREATE) {
                    boolean res = new DatabaseHelper(getApplicationContext()).addNewTransaction(transaction);

                    if (res) {
                        communicator.setLastTransaction(transaction);
                        new DatabaseHelper(getApplicationContext()).updateWallet(communicator.getCurrentWallet());
                        CustomizedToast.show(this, "New transaction is created");
                    } else
                        CustomizedToast.show(this, "Failed to create new transaction");

                } else if (communicator.getTransactionAddedRole() == TransactionAddedRole.ROLE_EDIT) {
                    transaction.setId(communicator.getLastTransaction().getId());
                    boolean res = new DatabaseHelper(getApplicationContext()).updateTransaction(transaction);

                    if (res) {
                        communicator.getLastTransaction().beginEdit()
                                        .setCategory(transaction.getCategory())
                                                .setDate(transaction.getDate())
                                                        .setAmount(transaction.getAmount())
                                                                .setNote(transaction.getNote())
                                                                        .setId(transaction.getId())
                                                                                .commitEdit();

                        communicator.setTransactionAddedRole(TransactionAddedRole.ROLE_CREATE);
                        new DatabaseHelper(getApplicationContext()).updateWallet(communicator.getCurrentWallet());
                        CustomizedToast.show(this, "Transaction is updated");
                    } else
                        CustomizedToast.show(this, "Failed to update transaction");
                }

                finish();
            }
        });
    }

    private void updateSelectedDate() {
        transaction_date.setText(SimpleDateHelper.simpleDateFormat(globalCalendar.getTime()));
    }
    private void displayPopupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                globalCalendar.set(Calendar.DAY_OF_MONTH, day);
                globalCalendar.set(Calendar.MONTH, month);
                globalCalendar.set(Calendar.YEAR, year);
                updateSelectedDate();
                CustomizedToast.show(AddTransactionActivity.this, "ondateset");
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(this
                , dateSetListener
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private Double tryGettingPrice() {
        Double amount = null;
        if (transaction_price.getText().toString().trim().equals(""))
            CustomizedToast.show(this, "Enter the price first");
        else
            amount = Double.parseDouble(transaction_price.getText().toString());
        return amount;
    }

    private Category tryGettingCategory() {
        Category category;
        String cat_txt = transaction_category_selector.getText().toString().trim();
        String[] tokens = cat_txt.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < tokens.length; i++) {
            if (i != 0)
                stringBuilder.append("_");
            stringBuilder.append(tokens[i]);
        }
        category = new Category(Category.getIdByName(stringBuilder.toString()));
        return category;
    }

//    private Date tryGettingDate() {
//        String date_txt = transaction_date.getText().toString().trim();
//        String[] tokens = date_txt.split("-");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, Integer.parseInt(tokens[2]));
//        calendar.set(Calendar.MONTH, Integer.parseInt(tokens[1]));
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokens[0]));
//        return calendar.getTime();
//    }

    private String tryGettingDetails() {
        return transaction_details.getText().toString().trim();
    }

//    private Event tryGettingEvent() {
//        String aliasName = transaction_event_selector.getText().toString().trim();
//        // get event from database
//        return new Event("none", null);
//    }

    private Transaction tryCreatingTransaction() {
        Double amount = tryGettingPrice();
        Category category = tryGettingCategory();
        String note = tryGettingDetails();
        //Date date = tryGettingDate();
        Date date = SimpleDateHelper.dateFromSimpleFormat(transaction_date.getText().toString());
        //Event event = tryGettingEvent();

        if (amount == null || category == null || date == null)
            return null;

        return new Transaction(amount, category, note, date, communicator.getCurrentWallet());
    }
}
package com.example.infinitepocket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.infinitepocket.adapters.DropDownMenuAdapter;
import com.example.infinitepocket.items.DropDownItem;
import com.example.infinitepocket.modelobjects.Category;
import com.example.infinitepocket.modelobjects.Event;
import com.example.infinitepocket.modelobjects.Transaction;
import com.example.infinitepocket.utilities.CustomizedToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {
    View back_transaction;
    TextView create_transaction;
    EditText transaction_created_date;
    AutoCompleteTextView transaction_category_selector;
    AutoCompleteTextView transaction_event_selector;

    Calendar globalCalendar = Calendar.getInstance();
    Communicator communicator = Communicator.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getSupportActionBar().hide();
        ini();
        iniViews();
        setListeners();
    }

    public AddTransactionActivity() {
        super();
    }

    private void ini() {
        back_transaction = findViewById(R.id.back_button_trans);
        create_transaction = findViewById(R.id.create_trans);
        transaction_created_date = findViewById(R.id.trans_created_date);
        transaction_category_selector = findViewById(R.id.trans_category_selector);
        transaction_event_selector = findViewById(R.id.trans_event_selector);
    }

    private void iniViews() {
        updateSelectedDate();

        // category selector
        List<DropDownItem> items = new ArrayList<>(Category.MAX_ID);
        for (int i = 0; i <= Category.MAX_ID; i++)
            items.add(new DropDownItem(Category.getFormattedName(i), Category.getIconId(i)));

        DropDownMenuAdapter cat_adapter = new DropDownMenuAdapter(this, items);
        transaction_category_selector.setAdapter(cat_adapter);

        // event selector
        List<DropDownItem> event_items = new ArrayList<>(10);
        event_items.add(new DropDownItem("None", R.drawable.ic_menu_wallet));

        DropDownMenuAdapter event_adapter = new DropDownMenuAdapter(this, event_items);
        transaction_event_selector.setAdapter(event_adapter);
    }

    private String formatAsString(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
    }

    private void setListeners() {
        back_transaction.setOnClickListener( view -> {
            finish();
        });
        transaction_created_date.setOnTouchListener( (view , event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                displayPopupDatePicker();
                updateSelectedDate();
            }
            return false;
        });
    }

    private void updateSelectedDate() {
        transaction_created_date.setText(formatAsString(globalCalendar));
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
}
package com.example.infinitepocket.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


// USE THIS INSTEAD OF THE DEFAULT TOAST FOR MORE VIBRANT COLORS
public abstract class CustomizedToast {
    private static final int textDefaultColor = Color.parseColor("#240046");
    private static final int backgroundDefaultColor = Color.parseColor("#cddafd");
    private static final boolean defaultShort = true;

    // show toast using default color
    public static void show(Context context, String message) {
        show(context, message, backgroundDefaultColor, textDefaultColor, defaultShort);
    }


    // show toast using user's colors and length
    public static void show(Context context, String message, int backgroundColor, int textColor, boolean isShort) {
        int duration = isShort ? 0 : 1;

        @SuppressLint("WrongConstant")
        Toast toast = Toast.makeText(context, message, duration);
        View view = toast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);

        //Gets the TextView from the Toast so it can be edited
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(textColor);
        toast.show();
    }
}

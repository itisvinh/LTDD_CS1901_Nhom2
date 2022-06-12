package com.example.infinitepocket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.infinitepocket.R;
import com.example.infinitepocket.items.DropDownItem;
import java.util.List;

public class DropDownMenuAdapter extends ArrayAdapter<DropDownItem> {

    public DropDownMenuAdapter(Context context, List<DropDownItem> list) {
        super(context,0 ,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return iniView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return iniView(position, convertView, parent);
    }

    private View iniView(int position, View view, ViewGroup viewGroupParent) {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.drop_down_item, viewGroupParent, false);

        View icon = view.findViewById(R.id.dropdown_item_icon);
        TextView textView = view.findViewById(R.id.dropdown_item_text);

        DropDownItem item = getItem(position);
        icon.setBackground(ContextCompat.getDrawable(getContext(), item.getIconId()));
        textView.setText(item.getItemName());

        return view;
    }
}

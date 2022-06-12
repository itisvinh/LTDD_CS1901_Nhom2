package com.example.infinitepocket.items;

public class DropDownItem {
    private final String itemName;
    private final int iconId;

    public DropDownItem(String itemName, int iconId) {
        this.itemName = itemName;
        this.iconId = iconId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getIconId() {
        return iconId;
    }
    public String toString() {
        return itemName;
    }
}

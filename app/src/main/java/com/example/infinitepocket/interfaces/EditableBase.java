package com.example.infinitepocket.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EditableBase<T> {
    protected boolean readyForEditing;
    private Map<Object, Object> unsavedChanges;

    {
        readyForEditing = false;
        unsavedChanges = new HashMap<>(3);
    }

    protected abstract T getInstance();

    public T beginEdit() {
        readyForEditing = true;
        return getInstance();
    }

    public void commitEdit() {
        if (!readyForEditing)
            throw new IllegalStateException("Call beginEdit() to start editing");
    }

    protected void addUnsavedChanges(Object key, Object value) {
        if (!readyForEditing)
            throw new IllegalStateException("Call beginEdit() to start editing");
        unsavedChanges.put(key, value);
    }

    protected Map.Entry getEntryFromKey(Object key) {
        for (Map.Entry entry : unsavedChanges.entrySet()) {
            if (entry.getKey() == key)
                return entry;
        }
        return null;
    }

    protected List<Map.Entry> getEntryFromValue(Object value) {
        List<Map.Entry> entries = new ArrayList<>();
        for (Map.Entry entry : unsavedChanges.entrySet()) {
            if (entry.getValue() == value)
                entries.add(entry);
        }

        if (entries.size() == 0)
            return null;
        return entries;
    }

}

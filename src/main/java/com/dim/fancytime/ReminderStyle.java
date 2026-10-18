package com.dim.fancytime;

public enum ReminderStyle {
    TEXT,
    TOAST;

    public String getDisplayName() {
        return switch (this) {
            case TEXT -> "Text";
            case TOAST -> "Toast";
        };
    }
}

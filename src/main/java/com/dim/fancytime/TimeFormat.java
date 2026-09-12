package com.dim.fancytime;

public enum TimeFormat {
    HOUR_24,
    HOUR_12;

    //Method to replace the raw enum config names in the buttons to more readable and nice ones (added in 1.1!)
    public String getDisplayName() {
        return switch (this) {
            case HOUR_24 -> "24-hour format";
            case HOUR_12 -> "12-hour format";
        };
    }
}

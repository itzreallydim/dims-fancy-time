package com.dim.fancytime;

public enum HudPosition {
    TOP_LEFT, //0
    CENTER, //1
    TOP_RIGHT; //2

    //Method to replace the raw enum config names in the buttons to more readable and nice ones (added in 1.1!)
    public String getDisplayName(){
        return switch (this) {
            case TOP_LEFT -> "Top Left";
            case CENTER -> "Center";
            case TOP_RIGHT -> "Top Right";
        };
    }
}

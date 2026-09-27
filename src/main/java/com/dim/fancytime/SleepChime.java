package com.dim.fancytime;

public enum SleepChime {
    ENABLED,
    DISABLED,
    ONLY_IN_SURVIVAL;

    public String getDisplayName() {
        return switch (this) {
            case ENABLED -> "Enabled";
            case DISABLED -> "Disabled";
            case ONLY_IN_SURVIVAL -> "Only in Survival";
        };
    }
}

package com.deflik.univswc281ocrutch.models;

public enum UniVSettingKeys {
    START_STOP_OPTION_KEY("is_start_stop_option_enabled"),
    EXHAUST_CONTROL_OPTION_KEY("is_exhaustcontrol_option_enabled");

    private final String key;

    UniVSettingKeys(String key) {
        this.key = key;
    }


    public String getKeyString() {
        return this.key;
    }
}

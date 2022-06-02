package com.cellulam.db.common;

public enum DataSourceType {
    HIKARI;

    public static DataSourceType getDefaultType() {
        return HIKARI;
    }
}

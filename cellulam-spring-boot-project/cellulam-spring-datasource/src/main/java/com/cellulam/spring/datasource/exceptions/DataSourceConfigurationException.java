package com.cellulam.spring.datasource.exceptions;

import org.springframework.dao.DataAccessException;

public class DataSourceConfigurationException extends DataAccessException {
    public DataSourceConfigurationException(String msg) {
        super("[DataSource Configuration Error] " + msg);
    }

    public DataSourceConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

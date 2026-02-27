package com.rems.common.base;


import com.rems.common.config.DBConfig;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {

    protected Connection connection;

    public BaseDAO(Connection connection) {
        this.connection = connection;
    }
}

package com.rems.common.transaction;

import com.rems.common.util.DBUtil;

import java.sql.Connection;
import java.util.function.Function;

public class TransactionManager {

    public <T> T execute(Function<Connection, T> action) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {
                T result = action.apply(conn);
                conn.commit();
                return result;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package com.rems.common.transaction;

import com.rems.common.util.DBUtil;

import java.sql.Connection;
import java.util.function.Supplier;

public class TransactionManager {

    public void execute(Runnable action) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            try {
                action.run();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                ConnectionHolder.clear();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T executeWithResult(Supplier<T> action) {

        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(false);

            try {
                T result = action.get();
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

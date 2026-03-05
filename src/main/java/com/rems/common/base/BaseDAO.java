//package com.rems.common.base;
//
//import com.rems.common.transaction.ConnectionHolder;
//
//import java.sql.Connection;
//
//public abstract class BaseDAO {
//
//    protected Connection getConnection() {
//        Connection conn = ConnectionHolder.get();
//
//        if (conn == null) {
//            throw new IllegalStateException(
//                    "No connection found. Did you forget to start transaction?"
//            );
//        }
//
//        return conn;
//    }
//}

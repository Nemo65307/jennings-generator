package com.nemo.jennings.dao;

import com.nemo.jennings.exception.ApplicationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBManager {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/oopviolw";
    private static final String USER = "oopviolw";
    private static final String PASS = "IsHDtOTnCqynwOIUVP1LMEPbOvsxVn35";

    public static Connection getDBConnection() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ApplicationException(e);
        }
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            throw new ApplicationException("Couldn't establish a connection!",  e);
        }
    }

}

package com.dette.core.bd;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database {
    void OpenConnection();
    void CloseConnection();
    ResultSet executeSelect() throws SQLException;
    int executeUpdate() throws SQLException;
    void initPreparedStatement(String sql) throws SQLException;
}
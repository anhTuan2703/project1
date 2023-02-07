package com.example.dao.impl;

import com.example.dao.GenericDAO;
import com.example.mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AbstractDAO<T> implements GenericDAO<T> {
    ResourceBundle myBundle = ResourceBundle.getBundle("db");
    public Connection getConnection() {
        try {
            Class.forName(myBundle.getString("driverName"));
            String sqlHostName = myBundle.getString("sqlHostName");
            String sqlInstanceName = myBundle.getString("sqlInstanceName");
            String sqlDatabase = myBundle.getString("sqlDatabase");

            String dbURL = "jdbc:sqlserver://" + sqlHostName + ":1433" + ";instance=" + sqlInstanceName + ";databaseName=" +
                    sqlDatabase + ";encrypt=true;trustServerCertificate=true;";
            String user = myBundle.getString("user");
            String pass = myBundle.getString("pass");
            return DriverManager.getConnection(dbURL, user, pass);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> query(String sql, RowMapper<T> rowMapper, Object... params) {
        List<T> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;


        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            setParameter(statement, params);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                results.add(rowMapper.mapRow(resultSet));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(connection != null) {
                    connection.close();
                }

                if(statement != null) {
                    statement.close();
                }

                if(resultSet != null) {
                    resultSet.close();
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Long insert(String sql, Object... params) {
        ResultSet rs = null;
        Long id = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameter(statement, params);
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();

            if(rs.next()) {
                id = rs.getLong(1);
            }

            connection.commit();
            return id;
        } catch (SQLException e) {
            System.out.println("Can't not insert into database");
            e.printStackTrace();

            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
                try {
                    if (connection != null) connection.close();
                    if (statement != null) statement.close();
                    if (rs != null) rs.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    @Override
    public void update(String sql, Object... params) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            setParameter(statement,params);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setParameter(PreparedStatement statement, Object... params) {
        try {
            for(int i = 0;i < params.length; i++) {
                Object parameter = params[i];
                int index = i + 1;
                if(parameter instanceof Long) {
                    statement.setLong(index, (Long) parameter);
                }

                else if(parameter instanceof String) {
                    statement.setString(index, (String) parameter);
                }

                else if(parameter instanceof Integer) {
                    statement.setInt(index, (Integer) parameter);
                }

                else if(parameter instanceof Date) {
                    statement.setDate(index, (Date) parameter);
                }

                else if(parameter == null) {
                    statement.setNull(index,Types.NVARCHAR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package pt.m0rais.crates.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {

    private final HikariDataSource source;

    public MySQL(String host, String database, String port, String user, String password) {
        source = new HikariDataSource();
        source.setMaximumPoolSize(10);
        source.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false");
        source.setUsername(user);
        source.setPassword(password);
        source.addDataSourceProperty("autoReconnect", "true");
    }

    public void close() {
        source.close();
    }

    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public void createTable(String table, String column) {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + table + " (" + column + ")");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}

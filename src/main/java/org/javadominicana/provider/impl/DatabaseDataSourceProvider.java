package org.javadominicana.provider.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javadominicana.RowData;
import org.javadominicana.provider.DataSourceProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 16/07/2024  - 01:24
 */
@Slf4j
@RequiredArgsConstructor
public class DatabaseDataSourceProvider implements DataSourceProvider {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;


    @Override
    public void initialize() throws IOException {
        log.info("Initializing database data source with URL '{}'", url);
        try {
            connection = DriverManager.getConnection(url, user, password);
            log.info("Database connection established.");
        } catch (Exception e) {
            log.error("Unable to connect to database", e);
            throw new IOException("Unable to connect to database", e);
        }
    }

    @Override
    public Iterable<RowData> getData() throws IOException {
        log.info("Fetching data from database.");
        List<RowData> data = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT name, workshop FROM table_name");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String workshop = resultSet.getString("workshop");
                data.add(new RowData(name, workshop));
                log.info("Read record: Name='{}', Workshop='{}'", name, workshop);
            }
        } catch (Exception e) {
            log.error("Error querying the database", e);
            throw new IOException("Error querying the database", e);
        }
        return data;
    }

    @Override
    public void close() throws IOException {
        try {
            if (connection != null) {
                connection.close();
                log.info("Database connection closed.");
            }
        } catch (Exception e) {
            log.error("Error closing database connection", e);
            throw new IOException("Error closing database connection", e);
        }
    }
}
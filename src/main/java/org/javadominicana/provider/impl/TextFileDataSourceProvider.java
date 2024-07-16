package org.javadominicana.provider.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javadominicana.RowData;
import org.javadominicana.provider.DataSourceProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 16/07/2024  - 01:23
 */
@Slf4j
@RequiredArgsConstructor
public class TextFileDataSourceProvider implements DataSourceProvider {

    private final String filePath;
    private BufferedReader reader;


    @Override
    public void initialize() throws IOException {
        log.info("Initializing text file data source from '{}'", filePath);
        reader = new BufferedReader(new FileReader(filePath));
        log.info("Text file data source initialized successfully.");
    }

    @Override
    public Iterable<RowData> getData() throws IOException {
        log.info("Fetching data from text file.");
        List<RowData> data = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                data.add(new RowData(parts[0], parts[1]));
                log.info("Read line: Name='{}', Workshop='{}'", parts[0], parts[1]);
            } else {
                log.warn("Invalid line format: '{}'. Skipping line.", line);
            }
        }
        return data;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            log.info("Text file data source closed.");
        }
    }
}

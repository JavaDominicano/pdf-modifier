package org.javadominicana.provider;

import org.javadominicana.RowData;

import java.io.IOException;

/**
 * @author me@fredpena.dev
 * @created 16/07/2024  - 01:20
 */
public interface DataSourceProvider {
    void initialize() throws IOException;

    Iterable<RowData> getData() throws IOException;

    void close() throws IOException;
}

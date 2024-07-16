package org.javadominicana.provider.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javadominicana.RowData;
import org.javadominicana.provider.DataSourceProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 16/07/2024  - 01:22
 */
@Slf4j
@RequiredArgsConstructor
public class ExcelDataSourceProvider implements DataSourceProvider {
    private final String filePath;
    private Workbook workbook;
    private Sheet sheet;


    @Override
    public void initialize() throws IOException {
        log.info("Initializing Excel data source from '{}'", filePath);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheetAt(0);
        log.info("Excel data source initialized successfully.");
    }

    @Override
    public Iterable<RowData> getData() {
        log.info("Fetching data from Excel sheet.");
        List<RowData> data = new ArrayList<>();
        for (Row row : sheet) {
            Cell nameCell = row.getCell(0);
            Cell workshopCell = row.getCell(1);
            if (nameCell != null && workshopCell != null) {
                String name = nameCell.getStringCellValue();
                String workshop = workshopCell.getStringCellValue();
                data.add(new RowData(name, workshop));
                log.info("Read row: Name='{}', Workshop='{}'", name, workshop);
            } else {
                log.warn("Null cell encountered in row {}. Skipping row.", row.getRowNum());
            }
        }
        return data;
    }

    @Override
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
            log.info("Excel data source closed.");
        }
    }
}
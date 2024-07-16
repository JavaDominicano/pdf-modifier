package org.javadominicana;

import org.javadominicana.provider.DataSourceProvider;
import org.javadominicana.provider.impl.ExcelDataSourceProvider;

/**
 * @author me@fredpena.dev
 * @created 16/07/2024  - 01:29
 */
public class Main {
    public static void main(String[] args) {
        String inputFilePath = "Source.xlsx";  // Or any other input file path
        String targetFileName = "Target.pdf";  // Your dynamic target PDF file name
        String outputDirectory = "";  // Your dynamic output directory

        DataSourceProvider dataSourceProvider = new ExcelDataSourceProvider(inputFilePath);
        PDFProcessor processor = new PDFProcessor(dataSourceProvider, targetFileName, outputDirectory);
        processor.process();

        // Alternatively, you can use:
        // DataSourceProvider dataSourceProvider = new TextFileDataSourceProvider("Source.txt");
        // DataSourceProvider dataSourceProvider = new DatabaseDataSourceProvider("jdbc:mysql://localhost:3306/db_name", "user", "password");


    }
}

package org.javadominicana;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.javadominicana.provider.DataSourceProvider;

import java.io.File;
import java.io.IOException;

/**
 * @author me@fredpena.dev
 * @created 16/07/2024  - 01:26
 */
@Slf4j
@RequiredArgsConstructor
public class PDFProcessor {
    private final DataSourceProvider dataSourceProvider;
    private final String targetFileName;
    private final String outputDirectory;


    public void process() {
        try {
            log.info("Starting data processing.");
            dataSourceProvider.initialize();
            for (RowData rowData : dataSourceProvider.getData()) {
                String target = targetFileName;
                String file = rowData.name().replaceAll("\\s+", "_");
                modifyPDF(target, file, rowData.name(), 360);
                modifyPDF(file + ".pdf", file, rowData.workshop(), 270);
            }
            log.info("Data processing completed.");
        } catch (IOException e) {
            log.error("Error during data processing", e);
        } finally {
            try {
                dataSourceProvider.close();
            } catch (IOException e) {
                log.error("Error closing data source", e);
            }
        }
    }

    private void modifyPDF(String inputFilePath, String outputFileName, String text, float yPosition) {
        try (PDDocument document = PDDocument.load(new File(inputFilePath))) {
            PDPage page = document.getPage(0);
            PDRectangle mediaBox = page.getMediaBox();
            float pageWidth = mediaBox.getWidth();

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                float fontSize = 14;
                float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000 * fontSize;
                float xPosition = (pageWidth - textWidth) / 2;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText(text);
                contentStream.endText();
                log.info("Added text '{}' to PDF at position ({}, {})", text, xPosition, yPosition);
            }

            String outputFilePath = outputDirectory + File.separator + outputFileName + ".pdf";
            document.save(outputFilePath);
            log.info("Saved modified PDF as '{}'", outputFilePath);
        } catch (IOException e) {
            log.error("Error modifying the PDF '{}'", inputFilePath, e);
        }
    }
}

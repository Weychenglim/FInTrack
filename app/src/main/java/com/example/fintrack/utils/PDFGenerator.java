package com.example.fintrack.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.fintrack.db.AccountItem;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.util.List;

public class PDFGenerator {

    public static void createPDF(Context context, List<AccountItem> accountList) {
        try {
            // Create a file in the Downloads directory
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFReports");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
            }
            File pdfFile = new File(pdfFolder, "AccountReport.pdf");

            // Initialize PDFWriter and Document
            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Add title
            Paragraph title = new Paragraph("Account Report")
                    .setBold()
                    .setFontSize(20)
                    .setMarginBottom(20)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Create a table with 8 columns
            float[] columnWidths = {2, 3, 2, 3, 2, 2, 2, 2};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            // Add table headers
            String[] headers = {"Type", "Remark", "Money", "Time", "Year", "Month", "Day", "Kind"};
            for (String header : headers) {
                table.addCell(new Cell().add(new Paragraph(header).setBold().setFontColor(ColorConstants.WHITE))
                        .setBackgroundColor(ColorConstants.BLUE));
            }

            // Populate the table
            for (AccountItem item : accountList) {
                table.addCell(item.getTypename());
                table.addCell(new Cell().add(new Paragraph(item.getTypename() != null ? item.getTypename() : "N/A")));
                table.addCell(String.valueOf(item.getMoney()));
                table.addCell(item.getTime());
                table.addCell(String.valueOf(item.getYear()));
                table.addCell(String.valueOf(item.getMonth()));
                table.addCell(String.valueOf(item.getDay()));
                table.addCell(item.getKind() == 0 ? "Expense" : "Income");
            }

            // Add table to the document
            document.add(table);

            // Close the document
            document.close();
            Toast.makeText(context, "PDF created/updated successfully at " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d("sdf",pdfFile.getAbsoluteFile().toString());
            openPdf(context,pdfFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openPdf(Context context, File file) {
        try {
            // Generate URI using FileProvider
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);

            // Create intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission

            // Start PDF viewer
            context.startActivity(Intent.createChooser(intent, "Open PDF"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to open PDF", Toast.LENGTH_SHORT).show();
        }
    }
}

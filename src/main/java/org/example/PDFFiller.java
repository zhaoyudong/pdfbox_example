package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Map;

public class PDFFiller {

    public void fillPdf(String templatePath, String outputPath, Map<String, String> fieldValues) {
        try (
                InputStream is = getClass().getResourceAsStream("/" + templatePath);
                PDDocument document = PDDocument.load(is)
        ) {
            PDAcroForm form = document.getDocumentCatalog().getAcroForm();

            if (form == null) {
                System.out.println("No form found in the PDF.");
                return;
            }

            // Allow setting fields even if they were originally read-only
            form.setNeedAppearances(true);

            for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
                PDField field = FieldUtils.findFieldRecursive(form, entry.getKey());
                if (field != null) {
                    field.setValue(entry.getValue());
                } else {
                    System.out.println("Field not found: " + entry.getKey());
                }
            }

            // Save the filled PDF
            try (OutputStream os = new FileOutputStream(outputPath)) {
                document.setAllSecurityToBeRemoved(true);
                document.save(os);
            }

            System.out.println("PDF saved to: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

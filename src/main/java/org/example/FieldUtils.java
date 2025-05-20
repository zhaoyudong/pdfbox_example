package org.example;

import org.apache.pdfbox.pdmodel.interactive.form.*;

public class FieldUtils {

    public static PDField findFieldRecursive(PDAcroForm form, String name) {
        for (PDField field : form.getFields()) {
            PDField result = searchField(field, name);
            if (result != null) return result;
        }
        return null;
    }

    private static PDField searchField(PDField field, String targetName) {
        if (field.getFullyQualifiedName().equals(targetName)) {
            return field;
        }
        if (field instanceof PDNonTerminalField) {
            for (PDField child : ((PDNonTerminalField) field).getChildren()) {
                PDField result = searchField(child, targetName);
                if (result != null) return result;
            }
        }
        return null;
    }
}

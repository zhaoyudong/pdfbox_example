package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFFieldExtractor {
    public static class FieldInfo {
        private final String name;
        private final String value;
        private final String partialName;
        private final String alternateFieldName;
        private final String fieldType;
        private final boolean readOnly;
        private final boolean required;
        private final String description;
        private final List<String> options;
        private final List<String> defaultValue;
        private final boolean checked;
        private final String onValue;

        public FieldInfo(String name, String value, String partialName,
                         String alternateFieldName, String fieldType,
                         boolean readOnly, boolean required, String description,
                         List<String> options, List<String> defaultValue,
                         boolean checked, String onValue) {
            this.name = name;
            this.value = value;
            this.partialName = partialName;
            this.alternateFieldName = alternateFieldName;
            this.fieldType = fieldType;
            this.readOnly = readOnly;
            this.required = required;
            this.description = description;
            this.options = options;
            this.defaultValue = defaultValue;
            this.checked = checked;
            this.onValue = onValue;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getPartialName() {
            return partialName;
        }

        public String getAlternateFieldName() {
            return alternateFieldName;
        }

        public String getFieldType() {
            return fieldType;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        public boolean isRequired() {
            return required;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getOptions() {
            return options;
        }

        public List<String> getDefaultValue() {
            return defaultValue;
        }

        public boolean isChecked() {
            return checked;
        }

        public String getOnValue() {
            return onValue;
        }
    }

    public List<FieldInfo> extractFields(String filePath) throws IOException {
        List<FieldInfo> fieldInfoList = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream("/" + filePath);
             PDDocument document = PDDocument.load(is)) {

            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                List<PDField> fields = acroForm.getFields();
                for (PDField field : fields) {
                    processField(field, fieldInfoList);
                }
            }
        }
        return fieldInfoList;
    }

    private void processField(PDField field, List<FieldInfo> fieldInfoList) {
        if (field instanceof PDNonTerminalField) {
            PDNonTerminalField nonTerminalField = (PDNonTerminalField) field;
            for (PDField childField : nonTerminalField.getChildren()) {
                processField(childField, fieldInfoList);
            }
        } else {
            addFieldInfo(fieldInfoList, field);
        }
    }

    private void addFieldInfo(List<FieldInfo> fieldInfoList, PDField field) {
        String value = field.getValueAsString();
        if (value == null) value = "[Empty]";

        String description = field.getAlternateFieldName();
        if (description == null) description = "[No Description]";

        List<String> options = new ArrayList<>();
        List<String> defaultValue = null;
        boolean checked = false;
        String onValue = "";

        if (field instanceof PDChoice) {
            PDChoice choice = (PDChoice) field;
            options = choice.getOptions();
            defaultValue = choice.getDefaultValue();
        }

        if (field instanceof PDCheckBox) {
            PDCheckBox checkBox = (PDCheckBox) field;
            checked = checkBox.isChecked();
            onValue = checkBox.getOnValue();
        }

        fieldInfoList.add(new FieldInfo(
                field.getFullyQualifiedName(),
                value,
                field.getPartialName(),
                field.getAlternateFieldName(),
                field.getClass().getSimpleName(),
                field.isReadOnly(),
                field.isRequired(),
                description,
                options,
                defaultValue,
                checked,
                onValue
        ));
    }
}
package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        PDFFieldExtractor extractor = new PDFFieldExtractor();
        List<PDFFieldExtractor.FieldInfo> fields = extractor.extractFields("g-28-empty.pdf");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fields));

        PDFFiller filler = new PDFFiller();
        Map<String, String> fieldValues = new HashMap<>();
//        {
//            "form1[0].#subform[0].Pt1Line2b_GivenName[0]": "San",
//            "form1[0].#subform[0].Pt1Line2a_FamilyName[0]": "Zhang",
//            "form1[0].#subform[0].Line3a_StreetNumber[0]": "4000",
//            "form1[0].#subform[0].Pt1Line2c_MiddleName[0]": "None",
//            "form1[0].#subform[0].Line3b_Unit[0]": "On"
//        }
        fieldValues.put("form1[0].#subform[0].Pt1Line2b_GivenName[0]", "San");
        fieldValues.put("form1[0].#subform[0].Pt1Line2a_FamilyName[0]", "Zhang");
        fieldValues.put("form1[0].#subform[0].Line3a_StreetNumber[0]", "4000");
        fieldValues.put("form1[0].#subform[0].Pt1Line2c_MiddleName[0]", "None");
        fieldValues.put("form1[0].#subform[0].Line3b_Unit[0]", " STE ");
        filler.fillPdf("g-28-empty.pdf", "g-28-filled.pdf", fieldValues);
    }

}

package com.ls.jr.printer;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;

public class GeneralPrinter {
    private static final Logger log = LoggerFactory.getLogger(GeneralPrinter.class);

    static Locale locale = new Locale("it", "IT");

    public JasperReport loadJasperReport(FileDescriptor file) throws Exception {

        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        InputStream jasperReportIs = fileStorageAPI.openStream(file);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportIs);
        return jasperReport;
    }
    public JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameters, Connection c) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, c);
        return jasperPrint;
    }


}

package com.ls.jr.printer;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.ls.jr.entity.ReportFile;
import com.ls.jr.exceptions.manager.NoSuchReportException;
import com.ls.jr.exceptions.report.PrintFailedException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GeneralPrinter {
    private static final Logger log = LoggerFactory.getLogger(GeneralPrinter.class);

    static Locale locale = new Locale("it", "IT");
    Persistence persistence = AppBeans.get(Persistence.class);

    public JasperReport loadMainJasperReport(List<ReportFile> files) throws Exception {
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        for ( ReportFile reportFile : files){
            if(!reportFile.getSubReport()){
                InputStream jasperReportIs = fileStorageAPI.openStream(reportFile.getFile());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportIs);
                return jasperReport;
            }
        }
        throw new NoSuchReportException("Report principale non trovato");
    }
    public List<JasperReport> loadSubJasperReport(List<ReportFile> files) throws Exception {
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        List<JasperReport> jasperReportList = new ArrayList<>();
        for ( ReportFile reportFile : files){
            if(reportFile.getSubReport()) {
                InputStream jasperReportIs = fileStorageAPI.openStream(reportFile.getFile());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportIs);
                jasperReportList.add(jasperReport);
            }
        }
         return jasperReportList;
    }

    public JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameters, Connection c) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, c);
        return jasperPrint;
    }
    public Connection getDbConnection(String store) throws PrintFailedException {
        Transaction tx;
        EntityManager entityManager;
        if(store!= null &&  store != "") {

            tx = persistence.createTransaction(store);
            entityManager = persistence.getEntityManager(store);

        }else{
            tx = persistence.createTransaction();
            entityManager = persistence.getEntityManager();
        }

        if(tx==null || entityManager==null)
            throw new PrintFailedException("Errore durante la creazione della tranzazione, store:"+store);

        return entityManager.getConnection();
    }


}

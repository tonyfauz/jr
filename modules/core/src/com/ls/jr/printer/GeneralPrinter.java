package com.ls.jr.printer;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.ls.jr.entity.Report;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.*;

public class GeneralPrinter {
    private static final Logger log = LoggerFactory.getLogger(GeneralPrinter.class);

    static Locale locale = new Locale("it", "IT");
    Persistence persistence = AppBeans.get(Persistence.class);

    public JasperPrint loadJasperPrint(Report report, HashMap<String, Object> params,Connection c) throws PrintFailedException {
        JasperPrint jasperPrint = null;
        java.util.Map parameters = new java.util.HashMap();

        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
        params.forEach( (key,value) ->{
            parameters.put(key,value);
        });

        try {
            JasperReport mainJasperReport = loadMainJasperReport(report.getFiles());
            Map<String, JasperReport> subJasperReport = loadSubJasperReport(report.getFiles());
            Map<String, BufferedImage> image = loadImage(report.getFiles());

            if (subJasperReport != null && !subJasperReport.isEmpty()) {
                subJasperReport.entrySet().forEach(stringJasperReportEntry -> {
                    parameters.put(stringJasperReportEntry.getKey(), stringJasperReportEntry.getValue());
                });
            }
            if (image != null && !image.isEmpty()) {
                image.entrySet().forEach(stringBufferedImageEntry -> {
                    parameters.put(stringBufferedImageEntry.getKey(), stringBufferedImageEntry.getValue());
                });
            }
           jasperPrint = getJasperPrint(mainJasperReport, (HashMap) parameters, c);
        }catch (Exception e ){
            log.debug(e.getMessage(),e);
            throw  new PrintFailedException("Errore durante la stampa del report",e);
        }
        return  jasperPrint;
    }

    public JasperPrint loadJasperPrintPerJBDataSource(Report report, HashMap<String, Object> params) throws PrintFailedException {
        JasperPrint jasperPrint = null;
        java.util.Map parameters = new java.util.HashMap();

        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
        params.forEach( (key,value) ->{
            parameters.put(key,value);
        });

        try {
            JasperReport mainJasperReport = loadMainJasperReport(report.getFiles());
            Map<String, JasperReport> subJasperReport = loadSubJasperReport(report.getFiles());
            Map<String, BufferedImage> image = loadImage(report.getFiles());

            if (subJasperReport != null && !subJasperReport.isEmpty()) {
                subJasperReport.entrySet().forEach(stringJasperReportEntry -> {
                    parameters.put(stringJasperReportEntry.getKey(), stringJasperReportEntry.getValue());
                });
            }
            if (image != null && !image.isEmpty()) {
                image.entrySet().forEach(stringBufferedImageEntry -> {
                    parameters.put(stringBufferedImageEntry.getKey(), stringBufferedImageEntry.getValue());
                });
            }
            jasperPrint = getJasperPrintEmpyDataSource(mainJasperReport, (HashMap) parameters);
        }catch (Exception e ){
            log.debug(e.getMessage(),e);
            throw  new PrintFailedException("Errore durante la stampa del report",e);
        }
        return  jasperPrint;
    }

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
    public Map<String,JasperReport> loadSubJasperReport(List<ReportFile> files) throws Exception {
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        Map<String,JasperReport>  parameterAndReport = new HashMap<>();
        for ( ReportFile reportFile : files){
            if(reportFile.getSubReport()) {
                InputStream jasperReportIs = fileStorageAPI.openStream(reportFile.getFile());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportIs);
                parameterAndReport.put(reportFile.getNomeParametro(),jasperReport);
            }
        }
        return parameterAndReport;
    }
    public Map<String,BufferedImage> loadImage(List<ReportFile> files) throws Exception {
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        Map<String,BufferedImage> imageMap = new HashMap<>();
        BufferedImage image = null;
        for ( ReportFile reportFile : files){
            if(reportFile.getLogo()!=null && reportFile.getLogo()){
                image = ImageIO.read(fileStorageAPI.openStream(reportFile.getFile()));
                imageMap.put(reportFile.getNomeParametro(),image);
            }
        }
        return imageMap;
    }

    public JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameters, Connection c) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, c);
        return jasperPrint;
    }
    public JasperPrint getJasperPrintEmpyDataSource(JasperReport jasperReport, HashMap parameters ) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new JREmptyDataSource());
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

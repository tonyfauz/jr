package com.ls.jr.printer;

import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.PrintFailedException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class ExcelPrinter extends GeneralPrinter implements Printer {


    private static final Logger log = LoggerFactory.getLogger(ExcelPrinter.class);

    @Override
    public byte[] printReport(Report report, HashMap<String, Object> params) throws PrintFailedException {


        byte[] bytes = null;
        java.util.Map parameters = new java.util.HashMap();

        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
        params.forEach( (key,value) ->{
            parameters.put(key,value);
        });

        try {

            Connection c = getDbConnection(report.getStore());

            if(c != null ) {

                JasperReport mainJasperReport = loadMainJasperReport(report.getFiles());
                List<JasperReport> subJasperReport = loadSubJasperReport(report.getFiles());

                if(subJasperReport!=null && !subJasperReport.isEmpty()){
                    for(JasperReport subJasper : subJasperReport){
                        parameters.put(subJasper.getName(),subJasper);
                    }
                }
                JasperPrint jasperPrint = getJasperPrint(mainJasperReport, (HashMap) parameters, c);

                bytes = exportXlsxToByteArray(jasperPrint);

            }else
                throw  new PrintFailedException("Connessione al db non riuscita");

        } catch (Exception e) {
            log.debug(e.getMessage(),e);
            throw  new PrintFailedException("Errore durante la stampa del report",e);
        }
        return bytes;
    }
    private byte[] exportXlsxToByteArray(JasperPrint jasperPrint) throws JRException {

        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bo));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        configuration.setWhitePageBackground(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        return bo.toByteArray();
    }



}

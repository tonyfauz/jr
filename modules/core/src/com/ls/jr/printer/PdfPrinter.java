package com.ls.jr.printer;


import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.PrintFailedException;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;


public class PdfPrinter extends GeneralPrinter implements Printer {

    private static final Logger log = LoggerFactory.getLogger(PdfPrinter.class);


    @Override
    public byte[] printReport(Report report, HashMap<String, Object> params) throws PrintFailedException {

        byte[] bytes = null;
        try {
            Connection c = getDbConnection(report.getStore());
            if (c != null) {
                JasperPrint jp = loadJasperPrint(report,params,c);
                bytes = exportPdfToByteArray(jp);
            } else
                throw new PrintFailedException("Connessione al db non riuscita");

        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new PrintFailedException("Errore durante la stampa del report", e);
        }
        return bytes;
    }

    private byte[] exportPdfToByteArray(JasperPrint jasperPrint) throws JRException {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


}

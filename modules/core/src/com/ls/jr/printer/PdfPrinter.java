package com.ls.jr.printer;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.global.AppBeans;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.PrintFailedException;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;

public class PdfPrinter extends GeneralPrinter implements ReportPrinter {

    private static final Logger log = LoggerFactory.getLogger(PdfPrinter.class);


    @Override
    public byte[] printReport(Report report, HashMap<String, Object> params) throws PrintFailedException {


        EntityManager em = AppBeans.get(EntityManager.class);

        byte[] bytes = null;
        java.util.Map parameters = new java.util.HashMap();

        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

        params.forEach((key, value) -> {
            parameters.put(key, value);
        });

        try {

            Connection c = em.getConnection();

            if (c != null) {

                JasperReport jasperReport = loadJasperReport(report.getFile());
                JasperPrint jasperPrint = getJasperPrint(jasperReport, (HashMap) parameters, c);
                bytes = exportPdfToByteArray(jasperPrint);

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

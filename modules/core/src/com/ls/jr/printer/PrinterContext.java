package com.ls.jr.printer;

import com.haulmont.cuba.core.global.FileStorageException;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.PrintFailedException;
import javafx.print.Printer;
import org.apache.commons.math3.analysis.function.Exp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PrinterContext {
    private static final Logger log = LoggerFactory.getLogger(PrinterContext.class);

    ReportPrinter reportPrinter;

    public void setReportPrinter(ReportPrinter reportPrinter) {
        this.reportPrinter = reportPrinter;
    }
    public byte[] printReport(Report report, HashMap<String, Object> params) throws PrintFailedException, FileStorageException {
        if(reportPrinter!=null)
            return reportPrinter.printReport(report,params);
        else
            throw new PrintFailedException("Problema di inizializzazione Report Printer");
    }
}

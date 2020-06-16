package com.ls.jr.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.LoadContext;
import com.ls.jr.database.ReportDatabaseAccess;
import com.ls.jr.entity.Report;
import com.ls.jr.entity.TipoExport;
import com.ls.jr.exceptions.manager.NoSuchReportException;
import com.ls.jr.exceptions.report.PrintFailedException;
import com.ls.jr.printer.ExcelPrinter;
import com.ls.jr.printer.PdfPrinter;
import com.ls.jr.printer.PrinterContext;
import com.ls.jr.printer.ReportPrinter;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;

@Service(ReportService.NAME)
public class ReportServiceBean implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceBean.class);

    @Inject
    private PrinterContext printerContext;
    @Inject
    private ReportDatabaseAccess reportDatabaseAccess;


    @Override
    public byte[] printReportFromAlias(String alias,HashMap<String,Object> params) throws PrintFailedException, FileStorageException {
        Report report = reportDatabaseAccess.findReportFromAlias(alias);
        return printReport(report,params);
    }


    @Override
    public byte[] printReport(Report report, HashMap<String,Object> params) throws PrintFailedException, FileStorageException {

        if(report.getTipo().equals(TipoExport.PDF))
             printerContext.setReportPrinter(new PdfPrinter());
        if(report.getTipo().equals(TipoExport.EXCEL))
             printerContext.setReportPrinter(new ExcelPrinter());

        return printerContext.printReport(report,params);

    }

    @Override
    public Report getReportDetail(Report report) throws NoSuchReportException {
        return null;
    }

    @Override
    public String getInfoParams(Report report) throws NoSuchReportException {
        return null;
    }

    @Override
    public Report getReportByAlias(String alias) throws NoSuchReportException {
        return null;
    }
}
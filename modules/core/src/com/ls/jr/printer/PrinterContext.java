package com.ls.jr.printer;

import com.haulmont.cuba.core.global.FileStorageException;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.PrintFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PrinterContext {
    private static final Logger log = LoggerFactory.getLogger(PrinterContext.class);

    Printer printer;

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
    public byte[] printReport(Report report, HashMap<String, Object> params) throws PrintFailedException, FileStorageException {
        if(printer !=null)
            return printer.printReport(report,params);
        else
            throw new PrintFailedException("Problema di inizializzazione Report Printer");
    }
}

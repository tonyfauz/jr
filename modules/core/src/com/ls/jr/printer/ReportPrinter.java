package com.ls.jr.printer;

import com.haulmont.cuba.core.global.FileStorageException;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.PrintFailedException;

import java.util.HashMap;

public interface ReportPrinter {

    public byte[] printReport(Report report, HashMap<String, Object> params) throws PrintFailedException, FileStorageException;
}

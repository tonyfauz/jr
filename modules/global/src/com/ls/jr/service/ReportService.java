 package com.ls.jr.service;

 import com.haulmont.cuba.core.global.FileStorageException;
 import com.ls.jr.entity.Report;
 import com.ls.jr.exceptions.manager.NoSuchReportException;
 import com.ls.jr.exceptions.report.PrintFailedException;

 import java.time.LocalDateTime;
 import java.util.HashMap;

 public interface ReportService {
    String NAME = "jr_ReportService";

    public byte[] printCurrentReportFromAlias(String alias, HashMap<String,Object> params) throws PrintFailedException, FileStorageException;
    public byte[] printReportFromAlias(String alias, LocalDateTime validitaFino, HashMap<String, Object> params) throws PrintFailedException, FileStorageException;
    public byte[] printReport(Report report, HashMap<String,Object> params) throws PrintFailedException, FileStorageException;
    public Report getReportDetail(Report report) throws  NoSuchReportException;
    public String getInfoParams(Report report) throws NoSuchReportException;
    public Report getReportByAlias(String alias) throws NoSuchReportException;

}
 package com.ls.jr.service;

 import com.ls.jr.entity.Report;
 import com.ls.jr.exceptions.manager.NoSuchReportException;
 import com.ls.jr.exceptions.report.PrintFailedException;

 import java.util.HashMap;

 public interface ReportService {
    String NAME = "jr_ReportService";

    public byte[] printReportFromAlias(String alias, HashMap<String,Object> params) throws PrintFailedException;
    public byte[] printReport(Report report, HashMap<String,Object> params) throws PrintFailedException;
    public Report getReportDetail(Report report) throws  NoSuchReportException;
    public String getInfoParams(Report report) throws NoSuchReportException;
    public Report getReportByAlias(String alias) throws NoSuchReportException;

}
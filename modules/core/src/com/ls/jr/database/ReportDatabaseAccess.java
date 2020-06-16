package com.ls.jr.database;

import com.ls.jr.entity.Report;

public interface ReportDatabaseAccess {
    String NAME = "jr_ReportDatabaseAccess";

    public Report findReportFromAlias(String alias);
}

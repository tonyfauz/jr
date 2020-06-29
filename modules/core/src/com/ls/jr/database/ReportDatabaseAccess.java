package com.ls.jr.database;

import com.ls.jr.entity.Report;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportDatabaseAccess {
    String NAME = "jr_ReportDatabaseAccess";
    /**
     * Restituisce il report in base all'alias e la data di fine validità.
     *
     **/
    public Report findReportFromAlias(String alias, LocalDateTime validoFino);
    /**
      * Restituisce il report in base all'alias considerando quello con fine validità nulla (corrente)
      *
      **/
    public Report findCurrentReportFromAlias(String alias);
}

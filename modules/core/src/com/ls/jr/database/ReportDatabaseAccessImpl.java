package com.ls.jr.database;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.ls.jr.entity.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(ReportDatabaseAccess.NAME)
public class ReportDatabaseAccessImpl implements ReportDatabaseAccess {
    private static final Logger log = LoggerFactory.getLogger(ReportDatabaseAccessImpl.class);

    @Inject
    private DataManager dataManager;

    private static final String REPORT_FROM_ALIAS = "select r form jr_Report r where alias = :aliasParam";

    @Override
    public Report findReportFromAlias(String alias) {
        LoadContext<Report> loadContext =  LoadContext.create(Report.class)
                .setQuery(LoadContext.createQuery(REPORT_FROM_ALIAS)
                        .setParameter("aliasParam",alias));
        return dataManager.load(loadContext);

    }
}

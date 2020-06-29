package com.ls.jr.database;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.ls.jr.entity.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Component(ReportDatabaseAccess.NAME)
public class ReportDatabaseAccessImpl implements ReportDatabaseAccess {
    private static final Logger log = LoggerFactory.getLogger(ReportDatabaseAccessImpl.class);

    @Inject
    private DataManager dataManager;

    private static final String REPORT_FROM_ALIAS = "select r from jr_Report r where r.validoFino = :validita and r.alias = :aliasParam";
    private static final String REPORT_VIEW = "report-view";

    @Override
    public Report findReportFromAlias(String alias, LocalDateTime validoFino) {
        LoadContext<Report> loadContext =  LoadContext.create(Report.class)
                .setQuery(LoadContext.createQuery(REPORT_FROM_ALIAS)
                        .setParameter("aliasParam",alias)
                        .setParameter("validita",validoFino)
                )
                .setView(REPORT_VIEW);
        return dataManager.load(loadContext);

    }

    @Override
    public Report findCurrentReportFromAlias(String alias) {
        return findReportFromAlias(alias, null);
    }
}

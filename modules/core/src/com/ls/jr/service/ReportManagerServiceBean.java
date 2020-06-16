package com.ls.jr.service;

import com.ls.jr.entity.Categoria;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.manager.SaveReportException;
import com.ls.jr.exceptions.report.DeleteReportException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(ReportManagerService.NAME)
public class ReportManagerServiceBean implements ReportManagerService {
    private static final Logger log = LoggerFactory.getLogger(ReportManagerServiceBean.class);

    @Override
    public Report saveReport(Report report) throws SaveReportException {
        return null;
    }

    @Override
    public void deleteReport(Report report) throws DeleteReportException {

    }

    @Override
    public Categoria saveCategoria(Categoria categoria) {
        return null;
    }

    @Override
    public void deleteCategoria(Categoria categoria) {

    }
}
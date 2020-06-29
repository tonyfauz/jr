package com.ls.jr.service;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.ls.jr.entity.Categoria;
import com.ls.jr.entity.Report;
import com.ls.jr.entity.ReportFile;
import com.ls.jr.exceptions.manager.*;
import com.ls.jr.exceptions.report.DeleteReportException;

import java.util.Set;

public interface ReportManagerService {
    String NAME = "jr_ReportManagerService";

    public Report saveReport(Report report) throws SaveReportException;
    public void deleteReport(Report report) throws DeleteReportException;

    public Categoria saveCategoria(Categoria categoria) throws SaveCategoriaException;
    public void deleteCategoria(Categoria categoria) throws DeleteCategoriaException;

    public Set<Entity> saveReportFile(CommitContext commitContext) throws SaveReportFileException;
    public void deleteReportFile(ReportFile reportFile) throws DeleteReportFileException;

    public boolean checkIfReportMasterExists(Report selectedReport);

}
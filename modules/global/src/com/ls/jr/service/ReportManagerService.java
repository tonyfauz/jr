package com.ls.jr.service;

import com.ls.jr.entity.Categoria;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.manager.DeleteCategoriaException;
import com.ls.jr.exceptions.manager.SaveCategoriaException;
import com.ls.jr.exceptions.report.DeleteReportException;
import com.ls.jr.exceptions.manager.SaveReportException;

public interface ReportManagerService {
    String NAME = "jr_ReportManagerService";

    public Report saveReport(Report report) throws SaveReportException;
    public void deleteReport(Report report) throws DeleteReportException;
    public Categoria saveCategoria(Categoria categoria) throws SaveCategoriaException;
    public void deleteCategoria(Categoria categoria) throws DeleteCategoriaException;

}
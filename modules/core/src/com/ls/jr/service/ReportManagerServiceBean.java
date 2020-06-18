package com.ls.jr.service;

import com.haulmont.cuba.core.global.DataManager;
import com.ls.jr.entity.Categoria;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.manager.DeleteCategoriaException;
import com.ls.jr.exceptions.manager.SaveCategoriaException;
import com.ls.jr.exceptions.manager.SaveReportException;
import com.ls.jr.exceptions.report.DeleteReportException;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Service(ReportManagerService.NAME)
public class ReportManagerServiceBean implements ReportManagerService {

    @Inject
    private DataManager dataManager;

    private static final Logger log = LoggerFactory.getLogger(ReportManagerServiceBean.class);

    @Override
    public Report saveReport(Report report) throws SaveReportException {
        return null;
    }

    @Override
    public void deleteReport(Report report) throws DeleteReportException {

    }

    @Override
    public Categoria saveCategoria(Categoria myCat) throws SaveCategoriaException {
        Categoria savedCat = null;
        if (myCat != null) {
            try {
                savedCat = dataManager.commit(myCat);
            } catch (Exception saveException) {
                throw new SaveCategoriaException("I dati non sono stati salvati correttamente.");
            }
        }
        return savedCat;
    }

    @Override
    public void deleteCategoria(Categoria catToRemove) throws DeleteCategoriaException {
        if(catToRemove != null) {
            try {
                dataManager.remove(catToRemove);
            } catch (Exception pkException){
                throw new DeleteCategoriaException("Non è consentito eliminare questa categoria");
            }
        }
    }
}
package com.ls.jr.service;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.ls.jr.entity.Categoria;
import com.ls.jr.entity.Report;
import com.ls.jr.entity.ReportFile;
import com.ls.jr.exceptions.manager.*;
import com.ls.jr.exceptions.report.DeleteReportException;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@Service(ReportManagerService.NAME)
public class ReportManagerServiceBean implements ReportManagerService {

    @Inject
    private DataManager dataManager;

    private static final Logger log = LoggerFactory.getLogger(ReportManagerServiceBean.class);

    @Override
    public Report saveReport(Report report) throws SaveReportException {
        Report savedRep = null;
        if (report != null) {
            try {
                savedRep = dataManager.commit(report);
            } catch (Exception saveException) {
                throw new SaveReportException("I dati non sono stati salvati correttamente.");
            }
        }
        return savedRep;
    }

    @Override
    public void deleteReport(Report reportToRemove) throws DeleteReportException {
        if(reportToRemove != null) {
            try {
                dataManager.remove(reportToRemove);
            } catch (Exception rfException) {
                throw new DeleteReportFileException("Non è consentito eliminare questo Report", rfException);
            }
        }
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

    @Override
    public  Set<Entity>  saveReportFile(CommitContext commitContext) throws SaveReportFileException {
        Set<Entity> myEntitySet;
        try {
            myEntitySet = dataManager.commit(commitContext);
        } catch (Exception saveException) {
            throw new SaveReportFileException("I dati non sono stati salvati correttamente.");
        }

        return myEntitySet;
    }

    @Override
    public void deleteReportFile(ReportFile reportFileToRemove) throws DeleteReportFileException {
        if(reportFileToRemove != null) {
            Boolean subReport = reportFileToRemove.getSubReport();
            if((subReport==null || !subReport) ) { //se è master report
                LoadContext<ReportFile> ldc = LoadContext.create(ReportFile.class)
                        .setQuery(LoadContext.createQuery("select e from jr_ReportFile e where e.report = :myReport and e.subReport = true")
                        .setParameter("myReport", reportFileToRemove.getReport()));
                List<ReportFile> listSubReport = dataManager.loadList(ldc);
                if (listSubReport.size() == 0) { //controllo che non abbia subreport
                    try {
                        dataManager.remove(reportFileToRemove);
                    } catch (Exception rfException){
                        throw new DeleteReportFileException("Non è consentito eliminare questo ReportFile", rfException);
                    }
                } else {
                    throw new DeleteReportFileException("Non è consentito eliminare file master se ci sono subReport");
                }
            } else {
                try {
                    dataManager.remove(reportFileToRemove);
                } catch (Exception rfException){
                    throw new DeleteReportFileException("Non è consentito eliminare questo ReportFile", rfException);
                }
            }
        }
    }

    @Override
    public boolean checkIfReportMasterExists(Report selectedReport) {
        LoadContext<ReportFile> ldc = LoadContext.create(ReportFile.class)
                .setQuery(LoadContext.createQuery("select e from jr_ReportFile e where e.report = :myReport and (e.subReport = false or e.subReport is null)")
                .setParameter("myReport", selectedReport));
        List<ReportFile> listMasterReport = dataManager.loadList(ldc);
        if (listMasterReport.size() >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfLogoReportExists(Report selectedReport) {
        LoadContext<ReportFile> ldc = LoadContext.create(ReportFile.class)
                .setQuery(LoadContext.createQuery("select e from jr_ReportFile e where e.report = :myReport and e.logo = true")
                        .setParameter("myReport", selectedReport));
        List<ReportFile> listLogoReport = dataManager.loadList(ldc);
        if (listLogoReport.size() >= 1) {
            return true;
        }
        return false;
    }

}
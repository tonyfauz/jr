package com.ls.jr.web.screens.reportfile;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.ls.jr.entity.Report;
import com.ls.jr.entity.ReportFile;
import com.ls.jr.exceptions.manager.SaveReportFileException;
import com.ls.jr.service.ReportManagerService;

import javax.inject.Inject;
import java.util.Set;

@UiController("jr_ReportFile.edit")
@UiDescriptor("report-file-edit.xml")
@EditedEntityContainer("reportFileDc")
@LoadDataBeforeShow
public class ReportFileEdit extends StandardEditor<ReportFile> {

    @Inject
    private Notifications notifications;
    @Inject
    private CheckBox subReportField;
    @Inject
    private Label<String> focusedReport;
    @Inject
    private ReportManagerService reportManagerService;
    @Inject
    private InstanceContainer<ReportFile> reportFileDc;
    private Report selectedReport;

    public void setEditReportFile(Report report) {
        selectedReport = report;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        focusedReport.setValue(selectedReport.getNome());
        getEditedEntity().setReport(selectedReport); //al fine di salvare il report associato a reportfile
    }

    @Subscribe("fileField")
    public void onFileFieldFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        notifications.create()
                .withType(Notifications.NotificationType.HUMANIZED)
                .withCaption("CARICAMENTO COMPLETATO")
                .withHideDelayMs(50)
                .show();
    }

    @Subscribe("fileField")
    public void onFileFieldFileUploadError(UploadField.FileUploadErrorEvent event) {
        notifications.create()
                .withType(Notifications.NotificationType.ERROR)
                .withCaption("ATTENZIONE")
                .withDescription("Il file non è stato caricato correttamente")
                .show();
    }

    public void onBtnSaveFileClick() {
        ReportFile myReportFile = reportFileDc.getItem();
        if (myReportFile != null) {
            boolean myRes = reportManagerService.checkIfReportMasterExists(selectedReport);
            if (!myRes || (myRes && subReportField.isChecked())) {
                try {
                    closeWithCommit();
                } catch (SaveReportFileException saveException) {
                    notifications.create()
                            .withType(Notifications.NotificationType.ERROR)
                            .withCaption("ATTENZIONE")
                            .withDescription("E' già presente un file master associato al report. Inserire il file come sub report.")
                            .show();
                }
            } else {
                notifications.create()
                        .withType(Notifications.NotificationType.ERROR)
                        .withCaption("ATTENZIONE")
                        .withDescription("E' già presente un file master associato al report. Inserire il file come sub report.")
                        .show();
            }
        }
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Entity> commitDelegate(CommitContext commitContext) {
        return reportManagerService.saveReportFile(commitContext);
    }
}
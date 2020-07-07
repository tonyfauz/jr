package com.ls.jr.web.screens.reportfile;

import com.google.common.collect.Sets;
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

import static com.haulmont.cuba.gui.Notifications.DELAY_DEFAULT;

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
    private CheckBox logoField;
    @Inject
    private TextField<String> parameterField;
    @Inject
    private FileUploadField fileField;
    @Inject
    private Label<String> focusedReport;
    @Inject
    private ReportManagerService reportManagerService;
    @Inject
    private InstanceContainer<ReportFile> reportFileDc;

    /*@Inject
    private Utils utils;*/

    private Report selectedReport;

    public Notifications.NotificationBuilder createNotification(Notifications.NotificationType notificationType,
                                                                String notificationCaption,
                                                                String notificationDescription,
                                                                Integer notificationHideDelayMs) {
        return notifications.create()
                .withType(notificationType)
                .withCaption(notificationCaption)
                .withDescription(notificationDescription)
                .withHideDelayMs(notificationHideDelayMs);
    }

    public void setEditReportFile(Report report) {
        selectedReport = report;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        focusedReport.setValue(selectedReport.getNome());
        getEditedEntity().setReport(selectedReport); //al fine di salvare il report associato a reportfile
    }

    public void checkExtensionFile(String loadedExtension) {
        if(logoField.isChecked()) {
            if(loadedExtension != null && (loadedExtension != ".png" || loadedExtension != ".jpg")) {
                fileField.setValue(null);
            }
        }
    }

    @Subscribe("logoField")
    public void onLogoFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {

        if (event.getValue().equals(true)) {
            fileField.setPermittedExtensions(Sets.newHashSet(".png", ".jpg"));
            subReportField.setValue(false);
            parameterField.setRequired(true);
            parameterField.setRequiredMessage("Campo Obbligatorio!");
            if(fileField.getValue() != null) {
                String loadedExtension = fileField.getValue().getExtension();
                checkExtensionFile(loadedExtension);
            }
        } else {
            fileField.setPermittedExtensions(Sets.newHashSet(".jrxml"));
            parameterField.setRequired(false);
        }
    }

    @Subscribe("subReportField")
    public void onSubReportFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue().equals(true)) {
            logoField.setValue(false);
            parameterField.setRequired(true);
            parameterField.setRequiredMessage("Campo Obbligatorio!");
            if(fileField.getValue() != null) {
                String loadedExtension = fileField.getValue().getExtension();
                checkExtensionFile(loadedExtension);
            }
        } else {
            parameterField.setRequired(false);
        }
    }


    @Subscribe("fileField")
    public void onFileFieldFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        createNotification(Notifications.NotificationType.HUMANIZED,
                "CARICAMENTO COMPLETATO",
                "",
                50).show();
    }

    @Subscribe("fileField")
    public void onFileFieldFileUploadError(UploadField.FileUploadErrorEvent event) {
        createNotification(Notifications.NotificationType.ERROR,
                "ATTENZIONE",
                "Il file non è stato caricato correttamente",
                DELAY_DEFAULT).show();
    }

    public void onBtnSaveFileClick() {
        ReportFile myReportFile = reportFileDc.getItem();
        if (myReportFile != null) {
            boolean reportMasterExists = reportManagerService.checkIfReportMasterExists(selectedReport);
            boolean reportLogoExists = reportManagerService.checkIfLogoReportExists(selectedReport);
            if (logoField.isChecked()) {
                if (!reportLogoExists) {
                    try {
                        closeWithCommit();
                    } catch (SaveReportFileException saveException) {
                        createNotification(Notifications.NotificationType.ERROR,
                                "ATTENZIONE",
                                "Il salvataggio non è andato a buon fine.",
                                DELAY_DEFAULT).show();
                    }
                } else {
                    createNotification(Notifications.NotificationType.ERROR,
                            "ATTENZIONE",
                            "Il logo associato al report è stato caricato.",
                            DELAY_DEFAULT).show();
                }
            } else {
                if (!reportMasterExists || (reportMasterExists && subReportField.isChecked())) {
                    try {
                        closeWithCommit();
                    } catch (SaveReportFileException saveException) {
                        createNotification(Notifications.NotificationType.ERROR,
                                "ATTENZIONE",
                                "Il salvataggio non è andato a buon fine.",
                                DELAY_DEFAULT).show();
                    }
                } else {
                    createNotification(Notifications.NotificationType.ERROR,
                            "ATTENZIONE",
                            "E' già presente un file master associato al report. Inserire il file come sub report.",
                            DELAY_DEFAULT).show();
                }
            }
        }
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Entity> commitDelegate(CommitContext commitContext) {
        return reportManagerService.saveReportFile(commitContext);
    }
}
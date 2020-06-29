package com.ls.jr.web.screens.reportfile;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.web.App;
import com.ls.jr.entity.Report;
import com.ls.jr.entity.ReportFile;
import com.ls.jr.exceptions.manager.DeleteReportFileException;
import com.ls.jr.service.ReportManagerService;

import javax.inject.Inject;

@UiController("jr_ReportFile.browse")
@UiDescriptor("report-file-browse.xml")
@LookupComponent("reportFilesTable")

public class ReportFileBrowse extends StandardLookup<ReportFile> {

    @Inject
    private CollectionLoader<ReportFile> reportFilesDl;
    @Inject
    private GroupTable<ReportFile> reportFilesTable;
    @Inject
    private Button editBtn;
    @Inject
    private Button removeBtn;
    @Inject
    private Screens screens;
    @Inject
    private Metadata metadata;
    @Inject
    private Notifications notifications;
    @Inject
    private Dialogs dialogs;
    @Inject
    private ReportManagerService reportManagerService;
    private Report selectedReport;

    public void setReport(Report report) {
        selectedReport = report;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        reportFilesDl.setQuery("select e from jr_ReportFile e where e.report = :myreport");
        reportFilesDl.setParameter("myreport", selectedReport);
        reportFilesDl.load();
    }

    @Subscribe("reportFilesTable")
    public void onReportFilesTableSelection(Table.SelectionEvent<ReportFile> event) {
        if (event.getSelected() != null) {
            editBtn.setEnabled(true);
            removeBtn.setEnabled(true);
        }
    }

    public void onCreateBtnClick() {
        Screen reportFileEditScreen = screens.create(ReportFileEdit.class);
        ((ReportFileEdit) reportFileEditScreen).setEntityToEdit(metadata.create(ReportFile.class));
        ((ReportFileEdit) reportFileEditScreen).setEditReportFile(selectedReport);
        ((ReportFileEdit) reportFileEditScreen).addAfterCloseListener(afterCloseEvent -> {
            reportFilesDl.load();
        });
        reportFileEditScreen.show();
    }

    public void onEditBtnClick() {
        Screen reportFileEditScreen = screens.create(ReportFileEdit.class);
        ((ReportFileEdit) reportFileEditScreen).setEntityToEdit(reportFilesTable.getSingleSelected());
        ((ReportFileEdit) reportFileEditScreen).setEditReportFile(selectedReport);
        ((ReportFileEdit) reportFileEditScreen).addAfterCloseListener(afterCloseEvent -> {
            reportFilesDl.load();
        });
        reportFileEditScreen.show();
    }

    public void onRemoveBtnClick() {
        ReportFile reportFileToRemove = reportFilesTable.getSingleSelected();
        if(reportFileToRemove != null) {
            dialogs.createOptionDialog()
                    .withCaption("Conferma la tua scelta")
                    .withMessage("Sei sicuro di voler eliminare il ReportFile selezionato?")
                    .withActions(
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("SI").withHandler(e -> {
                                try {
                                    reportManagerService.deleteReportFile(reportFileToRemove);
                                } catch (DeleteReportFileException rfException){
                                    notifications.create()
                                            .withType(Notifications.NotificationType.ERROR)
                                            .withCaption("ATTENZIONE")
                                            .withDescription(rfException.getMessage())
                                            .show();
                                }
                                reportFilesDl.load();
                            }),
                            new DialogAction(DialogAction.Type.NO),
                            new DialogAction(DialogAction.Type.CLOSE).withCaption("LogOut").withHandler(e -> {
                                App.getInstance().logout();
                            })
                    )
                    .show();
        } else {
            notifications.create()
                .withType(Notifications.NotificationType.WARNING)
                .withCaption("ATTENZIONE")
                .withDescription("Selezionare il ReportFile da eliminare.")
                .show();
        }
    }
}
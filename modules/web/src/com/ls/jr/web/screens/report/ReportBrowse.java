package com.ls.jr.web.screens.report;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.web.App;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.report.DeleteReportException;
import com.ls.jr.service.ReportManagerService;

import javax.inject.Inject;

@UiController("jr_Report.browse")
@UiDescriptor("report-browse.xml")
@LookupComponent("reportsTable")
@LoadDataBeforeShow
public class ReportBrowse extends StandardLookup<Report> {

    @Inject
    private GroupTable<Report> reportsTable;
    @Inject
    private Button removeBtn;
    @Inject
    private CollectionLoader<Report> reportsDl;
    @Inject
    private Notifications notifications;
    @Inject
    private Dialogs dialogs;
    @Inject
    private ReportManagerService reportManagerService;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        reportsDl.load();
    }

    @Subscribe("reportsTable")
    public void onReportsTableSelection(Table.SelectionEvent<Report> event) {
        if (event.getSelected() != null) {
            removeBtn.setEnabled(true);
        }
    }

    public void onRemoveBtnClick() {
        Report reportToRemove = reportsTable.getSingleSelected();
        if(reportToRemove != null) {
            dialogs.createOptionDialog()
                    .withCaption("Conferma la tua scelta")
                    .withMessage("Sei sicuro di voler eliminare la categoria selezionata?")
                    .withActions(
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("SI").withHandler(e -> {
                                try {
                                    reportManagerService.deleteReport(reportToRemove);
                                } catch (DeleteReportException rfException){
                                    notifications.create()
                                            .withType(Notifications.NotificationType.ERROR)
                                            .withCaption("ATTENZIONE")
                                            .withDescription(rfException.getMessage())
                                            .show();
                                }
                                reportsDl.load();
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
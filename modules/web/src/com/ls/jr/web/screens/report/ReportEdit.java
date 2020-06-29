package com.ls.jr.web.screens.report;

import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.ls.jr.entity.Categoria;
import com.ls.jr.entity.Report;
import com.ls.jr.exceptions.manager.SaveReportException;
import com.ls.jr.service.ReportManagerService;
import com.ls.jr.web.events.AutoUpdateCategoryEvent;
import com.ls.jr.web.screens.categoria.CategoryScreen;
import com.ls.jr.web.screens.reportfile.ReportFileBrowse;
import org.springframework.context.event.EventListener;

import javax.inject.Inject;

@UiController("jr_Report.edit")
@UiDescriptor("report-edit.xml")
@EditedEntityContainer("reportDc")
@LoadDataBeforeShow
public class ReportEdit extends StandardEditor<Report> {

    @Inject
    private LookupField<Categoria> categoriaField;
    @Inject
    private Button saveAction;
    @Inject
    private Button btnUploadFile;
    @Inject
    private Screens screens;
    @Inject
    private EntityStates entityStates;
    @Inject
    private InstanceContainer<Report> reportDc;

    @Inject
    private ReportManagerService reportManagerService;
    @Inject
    private Notifications notifications;

    public void onBtnCreateCategoryClick() {
        Screen categoryScreen = screens.create(CategoryScreen.class, OpenMode.DIALOG);
        categoryScreen.show();
    }

    public void onBtnUploadFileClick() {
        Screen reportFileScreen = screens.create(ReportFileBrowse.class, OpenMode.DIALOG);
        ((ReportFileBrowse) reportFileScreen).setReport(reportDc.getItem());
        reportFileScreen.show();
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if(!entityStates.isNew(reportDc.getItem())) {
            btnUploadFile.setVisible(true);
        }
    }

    @Subscribe("saveAction")
    public void onSaveActionClick(Button.ClickEvent event) {
        saveAction.setEnabled(false);
        btnUploadFile.setVisible(true);
        Report report = reportDc.getItem();
        if (report != null) {
            try {
                reportManagerService.saveReport(report);
            } catch (SaveReportException saveException) {
                notifications.create()
                        .withType(Notifications.NotificationType.ERROR)
                        .withCaption("ATTENZIONE")
                        .withDescription(saveException.getMessage())
                        .show();
            }
        }
    }

    @EventListener
    protected void onUserRemove(AutoUpdateCategoryEvent event) {
        categoriaField.setValue(event.getSavedCategory());
    }
}
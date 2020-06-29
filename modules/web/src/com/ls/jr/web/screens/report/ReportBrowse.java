package com.ls.jr.web.screens.report;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.screen.*;
import com.ls.jr.entity.Report;

import javax.inject.Inject;

@UiController("jr_Report.browse")
@UiDescriptor("report-browse.xml")
@LookupComponent("reportsTable")
@LoadDataBeforeShow
public class ReportBrowse extends StandardLookup<Report> {
    @Inject
    private Screens screens;

    @Subscribe
    public void onInit(InitEvent event) {

    }

}
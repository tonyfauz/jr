package com.ls.jr.web.screens.report;

import com.haulmont.cuba.gui.screen.*;
import com.ls.jr.entity.Report;

@UiController("jr_Report.edit")
@UiDescriptor("report-edit.xml")
@EditedEntityContainer("reportDc")
@LoadDataBeforeShow
public class ReportEdit extends StandardEditor<Report> {
}
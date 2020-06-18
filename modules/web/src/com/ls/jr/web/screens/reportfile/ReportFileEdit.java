package com.ls.jr.web.screens.reportfile;

import com.haulmont.cuba.gui.screen.*;
import com.ls.jr.entity.ReportFile;

@UiController("jr_ReportFile.edit")
@UiDescriptor("report-file-edit.xml")
@EditedEntityContainer("reportFileDc")
@LoadDataBeforeShow
public class ReportFileEdit extends StandardEditor<ReportFile> {
}
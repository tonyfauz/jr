package com.ls.jr.web.screens.reportfile;

import com.haulmont.cuba.gui.screen.*;
import com.ls.jr.entity.ReportFile;

@UiController("jr_ReportFile.browse")
@UiDescriptor("report-file-browse.xml")
@LookupComponent("reportFilesTable")
@LoadDataBeforeShow
public class ReportFileBrowse extends StandardLookup<ReportFile> {
}
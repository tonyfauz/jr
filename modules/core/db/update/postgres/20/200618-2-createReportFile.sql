alter table JR_REPORT_FILE add constraint FK_JR_REPORT_FILE_ON_REPORT foreign key (REPORT_ID) references JR_REPORT(ID);
alter table JR_REPORT_FILE add constraint FK_JR_REPORT_FILE_ON_FILE foreign key (FILE_ID) references SYS_FILE(ID);
create index IDX_JR_REPORT_FILE_ON_REPORT on JR_REPORT_FILE (REPORT_ID);
create index IDX_JR_REPORT_FILE_ON_FILE on JR_REPORT_FILE (FILE_ID);

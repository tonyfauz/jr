-- begin JR_CATEGORIA
alter table JR_CATEGORIA add constraint FK_JR_CATEGORIA_ON_PADRE foreign key (PADRE_ID) references JR_CATEGORIA(ID)^
create index IDX_JR_CATEGORIA_ON_PADRE on JR_CATEGORIA (PADRE_ID)^
-- end JR_CATEGORIA
-- begin JR_REPORT
alter table JR_REPORT add constraint FK_JR_REPORT_ON_CATEGORIA foreign key (CATEGORIA_ID) references JR_CATEGORIA(ID)^
create index IDX_JR_REPORT_ON_CATEGORIA on JR_REPORT (CATEGORIA_ID)^
-- end JR_REPORT
-- begin JR_REPORT_FILE
alter table JR_REPORT_FILE add constraint FK_JR_REPORT_FILE_ON_REPORT foreign key (REPORT_ID) references JR_REPORT(ID)^
alter table JR_REPORT_FILE add constraint FK_JR_REPORT_FILE_ON_FILE foreign key (FILE_ID) references SYS_FILE(ID)^
create index IDX_JR_REPORT_FILE_ON_REPORT on JR_REPORT_FILE (REPORT_ID)^
create index IDX_JR_REPORT_FILE_ON_FILE on JR_REPORT_FILE (FILE_ID)^
-- end JR_REPORT_FILE

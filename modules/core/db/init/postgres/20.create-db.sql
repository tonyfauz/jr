-- begin JR_CATEGORIA
alter table JR_CATEGORIA add constraint FK_JR_CATEGORIA_ON_PADRE foreign key (PADRE_ID) references JR_CATEGORIA(ID)^
create index IDX_JR_CATEGORIA_ON_PADRE on JR_CATEGORIA (PADRE_ID)^
-- end JR_CATEGORIA
-- begin JR_REPORT
alter table JR_REPORT add constraint FK_JR_REPORT_ON_CATEGORIA foreign key (CATEGORIA_ID) references JR_CATEGORIA(ID)^
alter table JR_REPORT add constraint FK_JR_REPORT_ON_FILE foreign key (FILE_ID) references SYS_FILE(ID)^
create unique index IDX_JR_REPORT_UK_ALIAS on JR_REPORT (ALIAS) where DELETE_TS is null ^
create index IDX_JR_REPORT_ON_CATEGORIA on JR_REPORT (CATEGORIA_ID)^
create index IDX_JR_REPORT_ON_FILE on JR_REPORT (FILE_ID)^
-- end JR_REPORT

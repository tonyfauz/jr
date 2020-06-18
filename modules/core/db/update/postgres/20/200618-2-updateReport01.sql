alter table JR_REPORT rename column file_id to file_id__u94796 ;
alter table JR_REPORT drop constraint FK_JR_REPORT_ON_FILE ;
drop index IDX_JR_REPORT_ON_FILE ;

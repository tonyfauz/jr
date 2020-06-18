create table JR_REPORT_FILE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    REPORT_ID uuid,
    FILE_ID uuid,
    SUB_REPORT boolean,
    --
    primary key (ID)
);
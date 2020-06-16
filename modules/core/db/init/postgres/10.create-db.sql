-- begin JR_CATEGORIA
create table JR_CATEGORIA (
    ID bigint,
    UUID uuid,
    --
    NOME varchar(255),
    PADRE_ID bigint,
    --
    primary key (ID)
)^
-- end JR_CATEGORIA
-- begin JR_REPORT
create table JR_REPORT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NOME varchar(255),
    CATEGORIA_ID bigint,
    TIPO integer,
    FILE_ID uuid,
    VALIDO_FINO timestamp,
    ALIAS varchar(255),
    STORE varchar(255),
    PARAMS varchar(500),
    --
    primary key (ID)
)^
-- end JR_REPORT

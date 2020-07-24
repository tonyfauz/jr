package com.ls.jr.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoDataSource implements EnumClass<Integer> {

    CONNESSIONE_JDBC(10),
    JAVA_BEANS(20);

    private Integer id;

    TipoDataSource(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static TipoDataSource fromId(Integer id) {
        for (TipoDataSource at : TipoDataSource.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
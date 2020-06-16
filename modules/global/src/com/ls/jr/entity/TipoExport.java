package com.ls.jr.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoExport implements EnumClass<Integer> {

    PDF(10),
    EXCEL(20);

    private Integer id;

    TipoExport(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static TipoExport fromId(Integer id) {
        for (TipoExport at : TipoExport.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
package com.ls.jr.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "JR_REPORT")
@Entity(name = "jr_Report")
public class Report extends StandardEntity {
    private static final long serialVersionUID = -9102800778100889404L;

    @Column(name = "NOME")
    protected String nome;

    @Lookup(type = LookupType.DROPDOWN, actions = {})
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORIA_ID")
    protected Categoria categoria;

    @Column(name = "TIPO")
    protected Integer tipo;

    @Column(name = "VALIDO_FINO")
    protected LocalDateTime validoFino;

    @Column(name = "VALIDO_DA")
    protected LocalDateTime validoDa;

    @CaseConversion(type = ConversionType.LOWER)
    @Column(name = "ALIAS")
    protected String alias;

    @Column(name = "STORE")
    protected String store;

    @Column(name = "PARAMS", length = 500)
    protected String params;

    public LocalDateTime getValidoDa() {
        return validoDa;
    }

    public void setValidoDa(LocalDateTime validoDa) {
        this.validoDa = validoDa;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getValidoFino() {
        return validoFino;
    }

    public void setValidoFino(LocalDateTime validoFino) {
        this.validoFino = validoFino;
    }

    public TipoExport getTipo() {
        return tipo == null ? null : TipoExport.fromId(tipo);
    }

    public void setTipo(TipoExport tipo) {
        this.tipo = tipo == null ? null : tipo.getId();
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
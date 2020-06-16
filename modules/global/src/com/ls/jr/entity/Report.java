package com.ls.jr.entity;

import com.haulmont.cuba.core.entity.FileDescriptor;
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

    @Lookup(type = LookupType.DROPDOWN, actions = {})
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID")
    protected FileDescriptor file;

    @Column(name = "VALIDO_FINO")
    protected LocalDateTime validoFino;

    @CaseConversion(type = ConversionType.LOWER)
    @Column(name = "ALIAS")
    protected String alias;

    @Column(name = "PARAMS", length = 500)
    protected String params;

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

    public FileDescriptor getFile() {
        return file;
    }

    public void setFile(FileDescriptor file) {
        this.file = file;
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
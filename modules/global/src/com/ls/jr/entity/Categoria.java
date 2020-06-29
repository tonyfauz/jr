package com.ls.jr.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseLongIdEntity;
import com.haulmont.cuba.core.entity.HasUuid;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@NamePattern("%s|nome")
@Table(name = "JR_CATEGORIA")
@Entity(name = "jr_Categoria")
public class Categoria extends BaseLongIdEntity implements HasUuid {
    private static final long serialVersionUID = 6975177183605770024L;

    @Column(name = "UUID")
    protected UUID uuid;

    @Column(name = "TEST")
    protected BigDecimal test;

    @CaseConversion
    @Column(name = "NOME")
    protected String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @Lookup(type = LookupType.DROPDOWN, actions = {})
    @OnDeleteInverse(DeletePolicy.DENY)
    @JoinColumn(name = "PADRE_ID")
    protected Categoria padre;

    public BigDecimal getTest() {
        return test;
    }

    public void setTest(BigDecimal test) {
        this.test = test;
    }

    public Categoria getPadre() {
        return padre;
    }

    public void setPadre(Categoria padre) {
        this.padre = padre;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
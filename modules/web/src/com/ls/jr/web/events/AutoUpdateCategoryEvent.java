package com.ls.jr.web.events;

import com.haulmont.cuba.gui.events.UiEvent;
import com.ls.jr.entity.Categoria;
import org.springframework.context.ApplicationEvent;

public class AutoUpdateCategoryEvent extends ApplicationEvent implements UiEvent {

    private Categoria categoria;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AutoUpdateCategoryEvent(Object source, Categoria categoria) {
        super(source);
        this.categoria = categoria;
    }


    public Categoria getSavedCategory() {
        return categoria;
    }
}

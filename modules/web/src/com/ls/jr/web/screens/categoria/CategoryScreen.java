package com.ls.jr.web.screens.categoria;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.App;
import com.ls.jr.entity.Categoria;
import com.ls.jr.exceptions.manager.DeleteCategoriaException;
import com.ls.jr.exceptions.manager.SaveCategoriaException;
import com.ls.jr.service.ReportManagerService;
import com.ls.jr.web.events.AutoUpdateCategoryEvent;

import javax.inject.Inject;

@UiController("jr_CategoryScreen")
@UiDescriptor("category-screen.xml")

@LoadDataBeforeShow
public class CategoryScreen extends Screen {
    @Inject
    private CollectionLoader<Categoria> categoriasDl;
    @Inject
    private InstanceContainer<Categoria> categoriaDc;
    @Inject
    private Tree<Categoria> categoryTree;
    @Inject
    private CheckBox isPadre;
    @Inject
    private TextField<String> nomeField;
    @Inject
    private Label<String> padreField;
    @Inject
    private HBoxLayout actionsPane;
    @Inject
    private Button saveBtn;
    @Inject
    private Button saveAndCloseBtn;
    @Inject
    private Notifications notifications;
    @Inject
    private Metadata metadata;
    @Inject
    private Dialogs dialogs;
    @Inject
    private ReportManagerService reportManagerService;
    @Inject
    private Events events;

    public void checkCampi() {
        if ((padreField.getValue()!=null && nomeField.getValue()!=null) || (isPadre.isChecked() && nomeField.getValue()!=null)) {
            enableBtn(true);
        } else {
            enableBtn(false);
        }
    }

    public void eraseField() {
        isPadre.setValue(false);
        categoriaDc.setItem(null);
        padreField.setValue("");
    }
    public void visible(Boolean setBoolean) {
        actionsPane.setVisible(setBoolean);
    }

    public void enable(Boolean setBoolean) {
        isPadre.setEnabled(setBoolean);
        nomeField.setEnabled(setBoolean);
    }

    public void enableBtn(Boolean setBoolean) {
        saveBtn.setEnabled(setBoolean);
        saveAndCloseBtn.setEnabled(setBoolean);
    }

    public void required(Boolean setBoolean) {
        nomeField.setRequired(setBoolean);
        nomeField.setRequiredMessage("Campo Obbligatorio!");
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        categoryTree.expandTree();
    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        categoryTree.addSelectionListener(categoriaSelectionEvent -> {
            if(categoriaDc.getItemOrNull() != null && nomeField.isEnabled()) {
                Categoria cat = categoryTree.getSingleSelected();
                if (cat != null) {
                    enable(true);
                    String catPadre = cat.getNome();
                    padreField.setValue(catPadre);
                    categoriaDc.getItem().setPadre(cat);
                }
            }
        });
    }

    @Subscribe("isPadre")
    public void onIsPadreValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            padreField.setValue("");
            categoriaDc.getItem().setPadre(null);
        } else {
            Categoria newCat = categoryTree.getSingleSelected();
            if(newCat != null) {
                String newCatPadre = newCat.getNome();
                padreField.setValue(newCatPadre);
                categoriaDc.getItem().setPadre(newCat);
            }
        }
        checkCampi();
    }

    @Subscribe("nomeField")
    public void onNomeFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        checkCampi();
    }

    @Subscribe("padreField")
    public void onPadreFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        checkCampi();
    }

    public void onCreateBtnClick() {
        categoriaDc.setItem(metadata.create(Categoria.class));
        enable(true);
        required(true);
        visible(true);
        if (padreField.getValue()==null && !isPadre.isChecked()) {
            notifications.create()
                .withType(Notifications.NotificationType.WARNING)
                .withCaption("ATTENZIONE")
                .withDescription("Selezionare almeno una categoria o identificare la nuova come categoria padre.")
                .show();
        } else {
            enableBtn(false);
        }

        Categoria cat = categoryTree.getSingleSelected();
        if (cat != null) {
            String catPadre = cat.getNome();
            padreField.setValue(catPadre);
            categoriaDc.getItem().setPadre(cat);
        }
    }

    public void onRemoveBtnClick() {
        Categoria catToRemove = categoryTree.getSingleSelected();
        if(catToRemove != null) {
            dialogs.createOptionDialog()
                    .withCaption("Conferma la tua scelta")
                    .withMessage("Sei sicuro di voler eliminare la categoria selezionata?")
                    .withActions(
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("SI").withHandler(e -> {
                                try {
                                    reportManagerService.deleteCategoria(catToRemove);
                                } catch (DeleteCategoriaException pkException){
                                    notifications.create()
                                            .withType(Notifications.NotificationType.ERROR)
                                            .withCaption("ATTENZIONE")
                                            .withDescription(pkException.getMessage())
                                            .show();
                                }
                                categoriasDl.load();
                            }),
                            new DialogAction(DialogAction.Type.NO),
                            new DialogAction(DialogAction.Type.CLOSE).withCaption("LogOut").withHandler(e -> {
                                App.getInstance().logout();
                            })
                    )
                    .show();
        } else {
            notifications.create()
                .withType(Notifications.NotificationType.WARNING)
                .withCaption("ATTENZIONE")
                .withDescription("Selezionare la categoria da eliminare.")
                .show();
        }
    }

    public void onSaveBtnClick() {
        Categoria myCat = categoriaDc.getItem();
        if (myCat != null) {
            try {
                reportManagerService.saveCategoria(myCat);
                eraseField();
                visible(false);
                enable(false);
                required(false);
                AutoUpdateCategoryEvent event = new AutoUpdateCategoryEvent(this, myCat);
                events.publish(event);
            } catch (SaveCategoriaException saveException) {
                notifications.create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withCaption("ATTENZIONE")
                    .withDescription(saveException.getMessage())
                    .show();
            }
        }
        categoriasDl.load();
    }
    public void onSaveAndCloseBtnClick() {
        Categoria myCat = categoriaDc.getItem();
        if (myCat != null) {
            try {
                reportManagerService.saveCategoria(myCat);
                eraseField();
                visible(false);
                enable(false);
                required(false);
                AutoUpdateCategoryEvent event = new AutoUpdateCategoryEvent(this, myCat);
                events.publish(event);
                close(WINDOW_DISCARD_AND_CLOSE_ACTION);
            } catch (SaveCategoriaException saveException) {
                notifications.create()
                        .withType(Notifications.NotificationType.ERROR)
                        .withCaption("ATTENZIONE")
                        .withDescription(saveException.getMessage())
                        .show();
            }
        }
        categoriasDl.load();
    }

    public void onCancelBtnClick() {
        dialogs.createOptionDialog()
            .withCaption("Conferma la tua scelta")
            .withMessage("Sei sicuro di voler uscire?")
            .withActions(
                new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withCaption("SI").withHandler(e -> {
                    this.closeWithDefaultAction();
                }),
                new DialogAction(DialogAction.Type.NO),
                new DialogAction(DialogAction.Type.CLOSE).withCaption("LogOut").withHandler(e -> {
                    App.getInstance().logout();
                })
            )
            .show();
    }


}
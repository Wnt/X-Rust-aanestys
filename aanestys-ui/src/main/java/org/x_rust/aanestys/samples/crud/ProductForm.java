package org.x_rust.aanestys.samples.crud;

import java.util.Collection;

import org.x_rust.aanestys.backend.data.Category;
import org.x_rust.aanestys.backend.data.Nominee;
import org.x_rust.aanestys.samples.backend.jpa.DataService;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A form for editing a single product.
 *
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
public class ProductForm extends CssLayout {

    TextField nomineeName = new TextField("Nominee name");
    CategoryField category = new CategoryField("Categories");
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    Button removeButton = new Button("Delete");
    private SampleCrudLogic viewLogic;
    private BeanFieldGroup<Nominee> fieldGroup;

    public ProductForm(SampleCrudLogic sampleCrudLogic) {
        viewLogic = sampleCrudLogic;
        addStyleName("product-form-wrapper");
        nomineeName.setWidth("100%");

        category.setWidth("100%");

        saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        cancelButton.addStyleName("cancel");
        removeButton.addStyleName(ValoTheme.BUTTON_DANGER);

        VerticalLayout layout = new VerticalLayout();
        layout.setHeight("100%");
        layout.setSpacing(true);
        layout.addStyleName("form-layout");

        VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSpacing(true);
        fieldLayout.addStyleName("product-form-fields");
        fieldLayout.addComponent(nomineeName);
        fieldLayout.addComponent(category);
        layout.addComponent(fieldLayout);
        layout.addComponent(saveButton);
        layout.addComponent(cancelButton);
        layout.addComponent(removeButton);

        layout.setExpandRatio(fieldLayout, 1);
        addComponent(layout);

        fieldGroup = new BeanFieldGroup<Nominee>(Nominee.class);
        fieldGroup.bindMemberFields(this);

        // perform validation and enable/disable buttons while editing
        ValueChangeListener valueListener = new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                formHasChanged();
            }
        };
        for (Field f : fieldGroup.getFields()) {
            f.addValueChangeListener(valueListener);
        }

        fieldGroup.addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent)
                    throws CommitException {
            }

            @Override
            public void postCommit(CommitEvent commitEvent)
                    throws CommitException {
                DataService.getInstance().update(
                        fieldGroup.getItemDataSource().getBean());
            }
        });

        saveButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();

                    // only if validation succeeds
                    Nominee product = fieldGroup.getItemDataSource().getBean();
                    viewLogic.saveProduct(product);
                } catch (CommitException e) {
                    Notification.show("Please re-check the fields",
                            Type.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.setClickShortcut(KeyCode.ESCAPE);
        cancelButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.cancelProduct();
            }
        });

        removeButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Nominee product = fieldGroup.getItemDataSource().getBean();
                viewLogic.deleteProduct(product);
            }
        });
    }

    public void setCategories(Collection<Category> categories) {
        category.setOptions(categories);
    }

    public void editProduct(Nominee product) {
        if (product == null) {
            product = new Nominee();
        }
        fieldGroup.setItemDataSource(new BeanItem<Nominee>(product));

        // before the user makes any changes, disable validation error indicator
        // of the product name field (which may be empty)
        nomineeName.setValidationVisible(false);
    }

    private void formHasChanged() {
        // show validation errors after the user has changed something
        nomineeName.setValidationVisible(true);

        // only products that have been saved should be removable
        boolean canRemoveProduct = false;
        BeanItem<Nominee> item = fieldGroup.getItemDataSource();
        if (item != null) {
            Nominee product = item.getBean();
            canRemoveProduct = product.getId() != -1;
        }
        removeButton.setEnabled(canRemoveProduct);
    }
}

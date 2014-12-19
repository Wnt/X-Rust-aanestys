package org.x_rust.aanestys.samples.crud;

import java.util.Collection;

import javax.ejb.EJB;

import org.x_rust.aanestys.backend.DataService;
import org.x_rust.aanestys.backend.data.Nominee;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A view for performing create-read-update-delete operations on products.
 * 
 * See also {@link SampleCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
public class SampleCrudView extends CssLayout implements View {

    public static final String VIEW_NAME = "Editor";
    private ProductTable table;
    private ProductForm form;

    private SampleCrudLogic viewLogic = new SampleCrudLogic(this);
    private Button newProduct;
    @EJB
	private DataService ds;

    public SampleCrudView() {
        setSizeFull();
        addStyleName("crud-view");
        HorizontalLayout topLayout = createTopBar();

        table = new ProductTable();
        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                viewLogic.rowSelected(table.getValue());
            }
        });

        form = new ProductForm(viewLogic);
        form.setCategories(ds.getAllCategories());

        VerticalLayout barAndTableLayout = new VerticalLayout();
        barAndTableLayout.addComponent(topLayout);
        barAndTableLayout.addComponent(table);
        barAndTableLayout.setMargin(true);
        barAndTableLayout.setSpacing(true);
        barAndTableLayout.setSizeFull();
        barAndTableLayout.setExpandRatio(table, 1);
        barAndTableLayout.setStyleName("crud-main-layout");

        addComponent(barAndTableLayout);
        addComponent(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        TextField filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                table.setFilter(event.getText());
            }
        });

        newProduct = new Button("New product");
        newProduct.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newProduct.setIcon(FontAwesome.PLUS_CIRCLE);
        newProduct.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.newProduct();
            }
        });

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);
        topLayout.setWidth("100%");
        topLayout.addComponent(filter);
        topLayout.addComponent(newProduct);
        topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(filter, 1);
        topLayout.setStyleName("top-bar");
        return topLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        viewLogic.enter(event.getParameters());
    }

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewProductEnabled(boolean enabled) {
        newProduct.setEnabled(enabled);
    }

    public void clearSelection() {
        table.setValue(null);
    }

    public void selectRow(Nominee row) {
        table.setValue(row);
    }

    public void editProduct(Nominee product) {
        if (product != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editProduct(product);
    }

    public Nominee getSelectedRow() {
        return table.getValue();
    }

    public void showProducts(Collection<Nominee> products) {
        BeanItemContainer<Nominee> container = table.getContainerDataSource();
        container.removeAllItems();
        container.addAll(products);
    }

}

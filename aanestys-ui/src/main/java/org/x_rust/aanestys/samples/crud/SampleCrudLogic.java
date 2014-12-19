package org.x_rust.aanestys.samples.crud;

import javax.ejb.EJB;

import org.x_rust.aanestys.AanestysUI;
import org.x_rust.aanestys.backend.DataService;
import org.x_rust.aanestys.backend.data.Nominee;

import com.vaadin.server.Page;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class SampleCrudLogic {

    private SampleCrudView view;
    @EJB
    private DataService ds;

    public SampleCrudLogic(SampleCrudView simpleCrudView) {
        view = simpleCrudView;
    }

    public void init() {
        editProduct(null);
        // Hide and disable if not admin
        if (!AanestysUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewProductEnabled(false);
        }

        refreshTable();
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
        view.editProduct(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String productId) {
        String fragmentParameter;
        if (productId == null || productId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = productId;
        }

        Page page = AanestysUI.get().getPage();
        page.setUriFragment("!" + SampleCrudView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }

    public void enter(String productId) {
        if (productId != null && !productId.isEmpty()) {
            if (productId.equals("new")) {
                newProduct();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    int pid = Integer.parseInt(productId);
                    Nominee product = findProduct(pid);
                    view.selectRow(product);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private Nominee findProduct(int productId) {
        return ds.getNomineeById(productId);
    }

    public void saveProduct(Nominee product) {
        view.showSaveNotification(product.getNomineeName() + " ("
                + product.getId() + ") updated");
        view.clearSelection();
        view.editProduct(null);
        refreshTable();
        setFragmentParameter("");
    }

    public void deleteProduct(Nominee nominee) {
        ds.deleteNominee(nominee);
        view.showSaveNotification(nominee.getNomineeName() + " ("
                + nominee.getId() + ") removed");

        view.clearSelection();
        view.editProduct(null);
        refreshTable();
        setFragmentParameter("");
    }

    public void editProduct(Nominee product) {
        if (product == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(product.getId() + "");
        }
        view.editProduct(product);
    }

    private void refreshTable() {
        Nominee oldSelection = view.getSelectedRow();
        view.showProducts(ds.getAllNominees());
        view.selectRow(oldSelection);
    }

    public void newProduct() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProduct(new Nominee());
    }

    public void rowSelected(Nominee product) {
        if (AanestysUI.get().getAccessControl().isUserInRole("admin")) {
            view.editProduct(product);
        }
    }
}

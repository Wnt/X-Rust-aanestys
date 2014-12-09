package org.x_rust.aanestys.samples.crud;

import org.x_rust.aanestys.samples.backend.data.Nominee;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Table of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ProductTable extends Table {

	private BeanItemContainer<Nominee> container;

	public ProductTable() {
		setSizeFull();
		addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);

		container = new BeanItemContainer<Nominee>(Nominee.class);
		setContainerDataSource(container);
		setVisibleColumns("id", "nomineeName", "category");
		setColumnHeaders("ID", "Nominee", "Categories");
		setColumnCollapsingAllowed(true);
		setColumnCollapsed("id", true);

		setColumnWidth("id", 50);
		setSelectable(true);
		setImmediate(true);
		// Show categories as a comma separated list
		setConverter("category", new CollectionToStringConverter());
	}

	@Override
	protected String formatPropertyValue(Object rowId, Object colId,
			Property property) {
		if (colId.equals("stockCount")) {
			Integer stock = (Integer) property.getValue();
			if (stock.equals(0)) {
				return "-";
			} else {
				return stock.toString();
			}
		}
		return super.formatPropertyValue(rowId, colId, property);
	}

	/**
	 * Filter the table based on a search string that is searched for in the
	 * product name, availability and category columns.
	 * 
	 * @param filterString
	 *            string to look for
	 */
	public void setFilter(String filterString) {
		container.removeAllContainerFilters();
		if (filterString.length() > 0) {
			SimpleStringFilter nameFilter = new SimpleStringFilter(
					"nomineeName", filterString, true, false);
			SimpleStringFilter categoryFilter = new SimpleStringFilter(
					"category", filterString, true, false);
			container.addContainerFilter(new Or(nameFilter, categoryFilter));
		}

	}

	@Override
	public Nominee getValue() {
		return (Nominee) super.getValue();
	}

	@Override
	public BeanItemContainer<Nominee> getContainerDataSource() {
		return container;
	}
}

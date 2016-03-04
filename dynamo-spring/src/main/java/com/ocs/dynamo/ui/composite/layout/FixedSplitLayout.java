package com.ocs.dynamo.ui.composite.layout;

import java.io.Serializable;
import java.util.Collection;

import com.ocs.dynamo.domain.AbstractEntity;
import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.service.BaseService;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.table.FixedTableWrapper;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.TextField;

/**
 * A layout for displaying a fixed collection of items, that contains both a
 * table view and a details view
 * 
 * @author bas.rutten
 * @param <ID>
 *            the type of the primary key
 * @param <T>
 *            the type of the entity
 */
@SuppressWarnings("serial")
public abstract class FixedSplitLayout<ID extends Serializable, T extends AbstractEntity<ID>>
        extends BaseSplitLayout<ID, T> {

	private static final long serialVersionUID = 4606800218149558500L;

	// the items to display in the table
	private Collection<T> items;

	/**
	 * Constructor
	 * 
	 * @param service
	 *            the service
	 * @param entityModel
	 *            the entity model that is used to construct the layout
	 * @param formOptions
	 *            formoptions that govern how the screen behaves
	 * @param fieldFilters
	 *            field filters applied to fields in the detail view
	 * @param sortOrder
	 */
	public FixedSplitLayout(BaseService<ID, T> service, EntityModel<T> entityModel,
	        FormOptions formOptions, SortOrder sortOrder) {
		super(service, entityModel, formOptions, sortOrder);
	}

	@Override
	protected void afterReload(T t) {
		getTableWrapper().getTable().select(t);
	}

	@Override
	protected final TextField constructSearchField() {
		// do nothing - not supported for this component
		return null;
	}

	@Override
	protected void constructTable() {
		// a table that displays a fixed collection of items
		FixedTableWrapper<ID, T> tw = new FixedTableWrapper<ID, T>(getService(), getEntityModel(),
		        getItems(), getSortOrder()) {
			@Override
			protected void onSelect(Object selected) {
				setSelectedItems(selected);
				checkButtonState(getSelectedItem());
				if (getSelectedItem() != null) {
					detailsView(getSelectedItem());
				}
			}
		};
		tw.build();
		setTableWrapper(tw);
	}

	public Collection<T> getItems() {
		return items;
	}

	/**
	 * The initialization consists of retrieving the required items
	 */
	@Override
	public void init() {
		this.items = loadItems();
	}

	/**
	 * Loads the items that are to be displayed
	 */
	protected abstract Collection<T> loadItems();

	/**
	 * Reloads the data after an update
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void reload() {
		super.reload();
		init();
		// remove all items from the container and add the new ones
		BeanItemContainer<T> beanContainer = (BeanItemContainer<T>) getTableWrapper()
		        .getContainer();
		beanContainer.removeAllItems();
		beanContainer.addAll(getItems());
	}

	@SuppressWarnings("unchecked")
	public void setSelectedItems(Object selectedItems) {
		if (selectedItems != null) {
			if (selectedItems instanceof Collection<?>) {
				Collection<?> col = (Collection<?>) selectedItems;
				T t = (T) col.iterator().next();
				// fetch the item again so that any details are loaded
				setSelectedItem(getService().fetchById(t.getId()));
			} else {
				// single item selected
				T t = (T) selectedItems;
				setSelectedItem(t);
			}
		} else {
			setSelectedItem(null);
			emptyDetailView();
		}
	}
}

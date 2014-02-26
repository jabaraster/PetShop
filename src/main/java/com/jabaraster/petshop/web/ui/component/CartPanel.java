/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.general.ArgUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.jabaraster.petshop.entity.ECart;
import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.entity.EOrder_;
import com.jabaraster.petshop.entity.EPet_;

/**
 * @author jabaraster
 */
public class CartPanel extends Panel {
    private static final long                            serialVersionUID = 8875179468600843699L;

    private final ECart                                  cart;

    private AjaxFallbackDefaultDataTable<EOrder, String> orders;

    /**
     * @param pId -
     * @param pCart -
     */
    public CartPanel(final String pId, final ECart pCart) {
        super(pId);
        this.cart = ArgUtil.checkNull(pCart, "pCart"); //$NON-NLS-1$
        this.add(getOrders());
    }

    private AjaxFallbackDefaultDataTable<EOrder, String> getOrders() {
        if (this.orders == null) {
            final List<IColumn<EOrder, String>> columns = new ArrayList<>();
            final ISortableDataProvider<EOrder, String> provider = new Provider();
            final int rowPerPage = 30;
            this.orders = new AjaxFallbackDefaultDataTable<>("orders", columns, provider, rowPerPage); //$NON-NLS-1$
        }
        return this.orders;
    }

    private class Provider extends SortableDataProvider<EOrder, String> {
        private static final long serialVersionUID = 2441285079114803927L;

        Provider() {
            this.setSort(EOrder_.pet.getName() + "." + EPet_.name.getName(), SortOrder.ASCENDING); //$NON-NLS-1$
        }

        @Override
        public Iterator<? extends EOrder> iterator(final long pFirst, final long pCount) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IModel<EOrder> model(final EOrder pObject) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long size() {
            // TODO Auto-generated method stub
            return 0;
        }

    }
}

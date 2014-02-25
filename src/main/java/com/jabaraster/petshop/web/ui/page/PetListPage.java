/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.wicket.Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPet_;
import com.jabaraster.petshop.service.IPetService;
import com.jabaraster.petshop.web.ui.component.AttributeColumn;

/**
 * @author jabaraster
 */
public class PetListPage extends RestrictedPageBase {
    private static final long                          serialVersionUID = -6810213540879254660L;

    @Inject
    IPetService                                        petService;

    private AjaxFallbackDefaultDataTable<EPet, String> pets;

    /**
     * 
     */
    public PetListPage() {
        this.add(getPets());
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ペット達"); //$NON-NLS-1$
    }

    private AjaxFallbackDefaultDataTable<EPet, String> getPets() {
        if (this.pets == null) {
            final List<IColumn<EPet, String>> columns = new ArrayList<>();
            columns.add(AttributeColumn.<EPet> sortable(EPet.getMeta(), EPet_.name));
            columns.add(new PropertyColumn<EPet, String>(Models.readOnly("種類"), "category.name", "category.name")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            this.pets = new AjaxFallbackDefaultDataTable<>("pets", columns, new Provider(), 20); //$NON-NLS-1$
        }
        return this.pets;
    }

    private class Provider extends SortableDataProvider<EPet, String> {
        private static final long serialVersionUID = 5996049732598983700L;

        Provider() {
            this.setSort(EPet_.name.getName(), SortOrder.ASCENDING);
        }

        @SuppressWarnings("boxing")
        @Override
        public Iterator<? extends EPet> iterator(final long pFirst, final long pCount) {
            Args.<Long> withinRange(0L, Long.valueOf(Integer.MAX_VALUE), pFirst, "pFirst"); //$NON-NLS-1$
            Args.<Long> withinRange(0L, Long.valueOf(Integer.MAX_VALUE), pCount, "pCount"); //$NON-NLS-1$
            return PetListPage.this.petService.fetch((int) pFirst, (int) pCount, getSort().getProperty(), getSort().isAscending()).iterator();
        }

        @Override
        public IModel<EPet> model(final EPet pObject) {
            return Models.of(pObject);
        }

        @Override
        public long size() {
            return PetListPage.this.petService.countAll();
        }
    }

}

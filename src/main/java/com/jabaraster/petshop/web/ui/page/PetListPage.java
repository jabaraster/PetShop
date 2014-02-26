/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.general.NotFound;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Args;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetImageData;
import com.jabaraster.petshop.entity.EPet_;
import com.jabaraster.petshop.service.ICartService;
import com.jabaraster.petshop.service.IPetService;
import com.jabaraster.petshop.web.LoginUserHolder;
import com.jabaraster.petshop.web.ui.component.AttributeColumn;
import com.jabaraster.petshop.web.ui.component.PetPanel;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class PetListPage extends RestrictedPageBase {
    private static final long                          serialVersionUID = -6810213540879254660L;

    @Inject
    IPetService                                        petService;
    @Inject
    ICartService                                       cartService;

    private final Handler                              handler          = new Handler();

    private DataView<EPet>                             pets2;
    private AjaxPagingNavigator                        petsNavigator;
    private AjaxFallbackDefaultDataTable<EPet, String> pets;

    /**
     * 
     */
    public PetListPage() {
        this.add(getPets());
        this.add(getPets2());
        this.add(getPetsNavigator());
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.RestrictedPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(ComponentCssHeaderItem.forType(PetListPage.class));
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ペット達"); //$NON-NLS-1$
    }

    private PetPanel createPetPanel(final EPet pPet) {
        final String ID = "pet"; //$NON-NLS-1$
        try {
            final EPetImageData imageData = PetListPage.this.petService.findImageDataByPet(pPet);
            return new PetPanel(ID, pPet, imageData);
        } catch (final NotFound e) {
            return new PetPanel(ID, pPet);
        }
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

    @SuppressWarnings("serial")
    private DataView<EPet> getPets2() {
        if (this.pets2 == null) {
            this.pets2 = new DataView<EPet>("pets2", new Provider()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final Item<EPet> pItem) {
                    final EPet pet = pItem.getModelObject();
                    final PetPanel panel = createPetPanel(pet);
                    panel.setOnThrowToCart(new IAjaxCallback() {
                        @Override
                        public void call(final AjaxRequestTarget pTarget) {
                            PetListPage.this.handler.onThrowToCart(pet, pTarget);
                        }
                    });
                    pItem.add(panel);
                }
            };
        }
        return this.pets2;
    }

    private AjaxPagingNavigator getPetsNavigator() {
        if (this.petsNavigator == null) {
            this.petsNavigator = new AjaxPagingNavigator("petsNavigator", getPets()); //$NON-NLS-1$
        }
        return this.petsNavigator;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 8116045922567165578L;

        void onThrowToCart(final EPet pPet, final AjaxRequestTarget pTarget) {
            final HttpSession session = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
            PetListPage.this.cartService.addOrder(LoginUserHolder.get(session), pPet);
        }
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

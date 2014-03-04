/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.general.NotFound;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetImageData;
import com.jabaraster.petshop.entity.EPet_;
import com.jabaraster.petshop.service.ICartService;
import com.jabaraster.petshop.service.IPetService;
import com.jabaraster.petshop.web.LoginUserHolder;
import com.jabaraster.petshop.web.ui.component.PetCategoriesPane;
import com.jabaraster.petshop.web.ui.component.PetPanel;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class PetListPage extends RestrictedPageBase {
    private static final long   serialVersionUID = -6810213540879254660L;

    @Inject
    IPetService                 petService;
    @Inject
    ICartService                cartService;

    private final Handler       handler          = new Handler();

    private PetCategoriesPane   categories;
    private WebMarkupContainer  petsContainer;
    private DataView<EPet>      pets;
    private AjaxPagingNavigator petsNavigator;

    /**
     * 
     */
    public PetListPage() {
        this.add(getCategories());
        this.add(getPetsContainer());
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

    @SuppressWarnings("serial")
    private PetCategoriesPane getCategories() {
        if (this.categories == null) {
            this.categories = new PetCategoriesPane("categories"); //$NON-NLS-1$
            this.categories.setOnSelect(new IAjaxCallback() {
                @Override
                public void call(final AjaxRequestTarget pTarget) {
                    PetListPage.this.handler.onCategorySelected(pTarget);
                }
            });
        }
        return this.categories;
    }

    @SuppressWarnings("serial")
    private DataView<EPet> getPets() {
        if (this.pets == null) {
            this.pets = new DataView<EPet>("pets", new Provider()) { //$NON-NLS-1$
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
        return this.pets;
    }

    private WebMarkupContainer getPetsContainer() {
        if (this.petsContainer == null) {
            this.petsContainer = new WebMarkupContainer("petsContainer"); //$NON-NLS-1$
            this.petsContainer.add(getPets());
        }
        return this.petsContainer;
    }

    private AjaxPagingNavigator getPetsNavigator() {
        if (this.petsNavigator == null) {
            this.petsNavigator = new AjaxPagingNavigator("petsNavigator", getPets()); //$NON-NLS-1$
        }
        return this.petsNavigator;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 8116045922567165578L;

        void onCategorySelected(final AjaxRequestTarget pTarget) {
            if (getCategories().getSelectedCategories().isEmpty()) {
                return;
            }
            pTarget.add(getPetsContainer());
        }

        void onThrowToCart(final EPet pPet, @SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
            PetListPage.this.cartService.addOrder(LoginUserHolder.get(getHttpSession()), pPet);
        }
    }

    private class Provider extends SortableDataProvider<EPet, String> {
        private static final long serialVersionUID = 5996049732598983700L;

        Provider() {
            this.setSort(EPet_.name.getName(), SortOrder.ASCENDING);
        }

        @Override
        public Iterator<? extends EPet> iterator(final long pFirst, final long pCount) {
            return PetListPage.this.petService.fetch( //
                    pFirst //
                    , pCount //
                    , getCategories().getSelectedCategories() //
                    , getSort().getProperty() //
                    , getSort().isAscending()) //
                    .iterator();
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

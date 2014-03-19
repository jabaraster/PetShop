/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.general.ArgUtil;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.NullAjaxCallback;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.jabaraster.petshop.entity.EPet;
import com.jabaraster.petshop.entity.EPetImageData;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.web.LoginUserHolder;
import com.jabaraster.petshop.web.ui.page.PetDeletePage;
import com.jabaraster.petshop.web.ui.page.PetEditPage;

/**
 * @author jabaraster
 */
public class PetPanel extends Panel {
    private static final long   serialVersionUID = 3970985863181318426L;

    private final Handler       handler          = new Handler();

    private final EPet          pet;
    private final EPetImageData imageData;

    private Form<?>             form;
    private Label               name;
    private WebMarkupContainer  image;
    private Label               unitPrice;
    private AjaxButton          cartThrower;

    private WebMarkupContainer  administrationMenuContainer;
    private Link<?>             goEdit;
    private Link<?>             goDelete;

    private IAjaxCallback       onThrowToCart    = NullAjaxCallback.GLOBAL;

    /**
     * @param pId -
     * @param pPet -
     */
    public PetPanel(final String pId, final EPet pPet) {
        super(pId);
        this.pet = ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        this.imageData = null;
        this.add(getForm());
    }

    /**
     * @param pId -
     * @param pPet -
     * @param pImageData -
     */
    public PetPanel(final String pId, final EPet pPet, final EPetImageData pImageData) {
        super(pId);
        this.pet = ArgUtil.checkNull(pPet, "pPet"); //$NON-NLS-1$
        this.imageData = ArgUtil.checkNull(pImageData, "pImageData"); //$NON-NLS-1$
        this.add(getForm());
    }

    /**
     * @param pCallback -
     */
    public void setOnThrowToCart(final IAjaxCallback pCallback) {
        this.onThrowToCart = pCallback == null ? NullAjaxCallback.GLOBAL : pCallback;
    }

    @SuppressWarnings("serial")
    private WebMarkupContainer getAdministrationMenuContainer() {
        if (this.administrationMenuContainer == null) {
            this.administrationMenuContainer = new WebMarkupContainer("administrationMenuContainer") { //$NON-NLS-1$
                @Override
                public boolean isVisible() {
                    final HttpServletRequest request = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
                    final LoginUser loginUser = LoginUserHolder.get(request.getSession());
                    return loginUser.isAdministrator();
                }
            };
            this.administrationMenuContainer.add(getGoEdit());
            this.administrationMenuContainer.add(getGoDelete());
        }
        return this.administrationMenuContainer;
    }

    @SuppressWarnings("serial")
    private AjaxButton getCartThrower() {
        if (this.cartThrower == null) {
            this.cartThrower = new IndicatingAjaxButton("cartThrower") { //$NON-NLS-1$
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    PetPanel.this.handler.onThrowToCart(pTarget);
                }
            };
        }
        return this.cartThrower;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getName());
            this.form.add(getImage());
            this.form.add(getUnitPrice());
            this.form.add(getCartThrower());
            this.form.add(getAdministrationMenuContainer());
        }
        return this.form;
    }

    private Link<?> getGoDelete() {
        if (this.goDelete == null) {
            final PageParameters param = PetDeletePage.createParmater(this.pet.getId().longValue());
            this.goDelete = new BookmarkablePageLink<>("goDelete", PetDeletePage.class, param); //$NON-NLS-1$
        }
        return this.goDelete;
    }

    private Link<?> getGoEdit() {
        if (this.goEdit == null) {
            final PageParameters param = PetEditPage.createParmater(this.pet.getId().longValue());
            this.goEdit = new BookmarkablePageLink<>("goEdit", PetEditPage.class, param); //$NON-NLS-1$
        }
        return this.goEdit;
    }

    private WebMarkupContainer getImage() {
        if (this.image == null) {
            final String ID = "image"; //$NON-NLS-1$
            if (this.imageData == null) {
                this.image = new WebMarkupContainer(ID);
                this.image.setVisible(false);
            } else {
                this.image = new WebMarkupContainer(ID);
                this.image.add(AttributeModifier.replace("src" //$NON-NLS-1$
                        , RequestCycle.get().getRequest().getContextPath() + "/rest/pet/images/" + this.imageData.getId().longValue())); //$NON-NLS-1$
            }
        }
        return this.image;
    }

    private Label getName() {
        if (this.name == null) {
            this.name = new Label("name", this.pet.getName()); //$NON-NLS-1$
        }
        return this.name;
    }

    private Label getUnitPrice() {
        if (this.unitPrice == null) {
            this.unitPrice = new Label("unitPrice", NumberFormat.getInstance().format(this.pet.getUnitPrice())); //$NON-NLS-1$
        }
        return this.unitPrice;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = -6981421051323368373L;

        void onThrowToCart(final AjaxRequestTarget pTarget) {
            PetPanel.this.onThrowToCart.call(pTarget);
        }
    }
}

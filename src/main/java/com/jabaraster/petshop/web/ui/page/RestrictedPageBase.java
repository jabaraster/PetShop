package com.jabaraster.petshop.web.ui.page;

import jabara.wicket.Models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.jabaraster.petshop.web.LoginUserHolder;
import com.jabaraster.petshop.web.ui.WicketApplication.MenuInfo;

/**
 *
 */
public abstract class RestrictedPageBase extends WebPageBase {
    private static final long serialVersionUID = 992616713060555L;

    /**
     * 
     */
    protected RestrictedPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected RestrictedPageBase(final PageParameters pParameters) {
        super(pParameters);
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#createRightAlignComponent(java.lang.String)
     */
    @Override
    protected Panel createRightAlignComponent(final String pId) {
        return new GoLogoutLink(pId);
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getLeftAlighMenusModel()
     */
    @Override
    protected IModel<? extends List<? extends MenuInfo>> getLeftAlighMenusModel() {
        final List<MenuInfo> menus = getPetShopApplication().getMenus();
        if (LoginUserHolder.get(getHttpSession()).isAdministrator()) {
            return Models.ofList(menus);
        }

        final List<MenuInfo> ret = new ArrayList<>();
        for (final MenuInfo menu : menus) {
            if (!AdministrationPageBase.class.isAssignableFrom(menu.getPageType())) {
                ret.add(menu);
            }
        }
        return Models.ofList(ret);
    }

    private static class GoLogoutLink extends Panel {
        private static final long serialVersionUID = -2325124434551475970L;

        public GoLogoutLink(final String pId) {
            super(pId);
            this.add(new BookmarkablePageLink<>("goLogout", LogoutPage.class)); //$NON-NLS-1$
        }
    }
}

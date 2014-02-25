package com.jabaraster.petshop.web.ui.page;

import jabara.wicket.ComponentCssHeaderItem;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.jabaraster.petshop.Environment;
import com.jabaraster.petshop.web.ui.WicketApplication;
import com.jabaraster.petshop.web.ui.WicketApplication.MenuInfo;

/**
 *
 */
public abstract class RestrictedPageBase extends WebPageBase {
    private static final long  serialVersionUID = 992616713060555L;

    private Label              applicationName;
    private Link<?>            goTop;
    private Link<?>            goLogout;
    private ListView<MenuInfo> menus;

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
        this.add(getGoTop());
        this.add(getGoLogout());
        this.add(getMenus());
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(ComponentCssHeaderItem.forType(RestrictedPageBase.class));
    }

    /**
     * このメソッドはサブクラスでコンポーネントIDの重複を避けるためにprotectedにしています. <br>
     * 
     * @return -
     */
    protected Link<?> getGoLogout() {
        if (this.goLogout == null) {
            this.goLogout = new BookmarkablePageLink<>("goLogout", LogoutPage.class); //$NON-NLS-1$
        }
        return this.goLogout;
    }

    private Label getApplicationName() {
        if (this.applicationName == null) {
            this.applicationName = new Label("applicationName", Environment.getApplicationName()); //$NON-NLS-1$
        }
        return this.applicationName;
    }

    private Link<?> getGoTop() {
        if (this.goTop == null) {
            this.goTop = new BookmarkablePageLink<>("goTop", Application.get().getHomePage()); //$NON-NLS-1$
            this.goTop.add(getApplicationName());
        }
        return this.goTop;
    }

    @SuppressWarnings("serial")
    private ListView<MenuInfo> getMenus() {
        if (this.menus == null) {
            this.menus = new ListView<WicketApplication.MenuInfo>("menus", getPetShopApplication().getMenus()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<MenuInfo> pItem) {
                    final MenuInfo menu = pItem.getModelObject();
                    final Link<?> goPage = new BookmarkablePageLink<>("goPage", menu.getPageType()); //$NON-NLS-1$
                    goPage.add(new Label("label", menu.getTitleModel())); //$NON-NLS-1$
                    pItem.add(goPage);
                }
            };
        }
        return this.menus;
    }
}

/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.jabaraster.petshop.web.ui.WicketApplication;
import com.jabaraster.petshop.web.ui.WicketApplication.MenuInfo;
import com.jabaraster.petshop.web.ui.page.LogoutPage;

/**
 * @author jabaraster
 */
public class MenuPanel extends Panel {
    private static final long    serialVersionUID = -5666575125401739571L;

    private final List<MenuInfo> menusValue;

    private Link<?>              goLogout;
    private ListView<MenuInfo>   menus;

    /**
     * @param pId -
     * @param pMenusValue -
     */
    public MenuPanel(final String pId, final List<MenuInfo> pMenusValue) {
        super(pId);
        this.menusValue = new ArrayList<>(pMenusValue);

        this.add(getGoLogout());
        this.add(getMenus());
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

    @SuppressWarnings("serial")
    private ListView<MenuInfo> getMenus() {
        if (this.menus == null) {
            this.menus = new ListView<WicketApplication.MenuInfo>("menus", this.menusValue) { //$NON-NLS-1$
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

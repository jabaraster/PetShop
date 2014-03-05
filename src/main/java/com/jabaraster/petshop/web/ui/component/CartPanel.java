/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.general.ArgUtil;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.jabaraster.petshop.entity.EOrder;
import com.jabaraster.petshop.model.LoginUser;
import com.jabaraster.petshop.service.ICartService;

/**
 * @author jabaraster
 */
public class CartPanel extends Panel {
    private static final long serialVersionUID = 8875179468600843699L;

    @Inject
    ICartService              cartService;

    private final LoginUser   loginUser;

    private ListView<EOrder>  orders;

    /**
     * @param pId -
     * @param pLoginUser -
     */
    public CartPanel(final String pId, final LoginUser pLoginUser) {
        super(pId);
        this.loginUser = ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$

        this.add(getOrders());
    }

    @SuppressWarnings("serial")
    private ListView<EOrder> getOrders() {
        if (this.orders == null) {
            this.orders = new ListView<EOrder>("orders", new CartModel()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<EOrder> pItem) {
                    final EOrder order = pItem.getModelObject();
                    pItem.add(new Label("petName", order.getPet().getName())); //$NON-NLS-1$
                    pItem.add(new Label("quantity", Integer.valueOf(order.getQuantity()))); //$NON-NLS-1$
                }
            };
        }
        return this.orders;
    }

    private class CartModel extends LoadableDetachableModel<List<EOrder>> {
        private static final long serialVersionUID = -767694712606611459L;

        @Override
        protected List<EOrder> load() {
            return CartPanel.this.cartService.findByUserId(CartPanel.this.loginUser.getId()).getOrders();
        }
    }
}

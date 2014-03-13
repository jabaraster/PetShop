package com.jabaraster.petshop.web.ui.page;

import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;

import com.jabaraster.petshop.model.FailAuthentication;
import com.jabaraster.petshop.web.ui.AppSession;
import com.jabaraster.petshop.web.ui.WicketApplication.MenuInfo;
import com.jabaraster.petshop.web.ui.component.LoginPanel;
import com.jabaraster.petshop.web.ui.component.LoginPanel.Credential;

/**
 * 
 */
@SuppressWarnings("synthetic-access")
public class LoginPage extends WebPageBase {
    private static final long serialVersionUID = -549716745755288187L;

    private final Handler     handler          = new Handler();

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(ComponentJavaScriptHeaderItem.minimizedForType(LoginPage.class));
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#createRightAlignComponent(java.lang.String)
     */
    @SuppressWarnings("serial")
    @Override
    protected Panel createRightAlignComponent(final String pId) {
        final LoginPanel pane = new LoginPanel(pId);
        pane.setOnSubmit(new IAjaxCallback() {
            @Override
            public void call(@SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
                LoginPage.this.handler.tryLogin();
            }
        });
        return pane;
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getLeftAlighMenusModel()
     */
    @Override
    protected IModel<? extends List<? extends MenuInfo>> getLeftAlighMenusModel() {
        return Models.ofList(Collections.<MenuInfo> emptyList());
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly(getString("pageTitle")); //$NON-NLS-1$
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = -5583450382246308222L;

        void tryLogin() {
            try {
                final Credential credential = ((LoginPanel) getRightAlignPanel()).getCredential();
                AppSession.get().login(credential.getUserId(), credential.getPassword());

                final StringValue url = getPageParameters().get("u"); //$NON-NLS-1$
                if (!url.isEmpty() && !url.isNull()) {
                    setResponsePage(new RedirectPage(url.toString()));
                } else {
                    setResponsePage(getApplication().getHomePage());
                }
            } catch (final FailAuthentication e) {
                error(getString("message.failLogin")); //$NON-NLS-1$
            }
        }
    }
}

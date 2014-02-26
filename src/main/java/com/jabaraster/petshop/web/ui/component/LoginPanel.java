/**
 * 
 */
package com.jabaraster.petshop.web.ui.component;

import jabara.general.Empty;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;
import jabara.wicket.NullAjaxCallback;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class LoginPanel extends Panel {
    private static final long serialVersionUID = 953226391750385587L;

    private final Handler     handler          = new Handler();

    private StatelessForm<?>  form;
    private TextField<String> userId;
    private PasswordTextField password;
    private AjaxButton        submitter;

    private IAjaxCallback     onError          = NullAjaxCallback.GLOBAL;
    private IAjaxCallback     onSubmit         = NullAjaxCallback.GLOBAL;

    /**
     * @param pId -
     */
    public LoginPanel(final String pId) {
        super(pId);
        this.add(getForm());
    }

    /**
     * @return -
     */
    public Credential getCredential() {
        return new Credential(getUserId().getModelObject(), getPassword().getModelObject());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        JavaScriptUtil.addFocusScript(pResponse, getUserId());
    }

    /**
     * @param pOnError -
     */
    public void setOnError(final IAjaxCallback pOnError) {
        this.onError = pOnError == null ? NullAjaxCallback.GLOBAL : pOnError;
    }

    /**
     * @param pOnSubmit -
     */
    public void setOnSubmit(final IAjaxCallback pOnSubmit) {
        this.onSubmit = pOnSubmit == null ? NullAjaxCallback.GLOBAL : pOnSubmit;
    }

    private StatelessForm<?> getForm() {
        if (this.form == null) {
            this.form = new StatelessForm<>("form"); //$NON-NLS-1$
            this.form.add(getUserId());
            this.form.add(getPassword());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            this.password = new PasswordTextField("password", Models.of(Empty.STRING)); //$NON-NLS-1$
        }
        return this.password;
    }

    @SuppressWarnings({ "serial" })
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    LoginPanel.this.handler.onError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    LoginPanel.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getUserId() {
        if (this.userId == null) {
            this.userId = new TextField<>("userId", Models.of(Empty.STRING)); //$NON-NLS-1$
            this.userId.setRequired(true);
        }
        return this.userId;
    }

    /**
     * @author jabaraster
     */
    public static class Credential implements Serializable {
        private static final long serialVersionUID = 449610417304787960L;

        private final String      userId;
        private final String      password;

        /**
         * @param pUserId -
         * @param pPassword -
         */
        public Credential(final String pUserId, final String pPassword) {
            this.userId = pUserId;
            this.password = pPassword;
        }

        /**
         * @return -
         */
        public String getPassword() {
            return this.password;
        }

        /**
         * @return -
         */
        public String getUserId() {
            return this.userId;
        }
    }

    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 1515009177044918108L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender();

        void onError(final AjaxRequestTarget pTarget) {
            processAjax(pTarget);
            LoginPanel.this.onError.call(pTarget);
        }

        void onSubmit(final AjaxRequestTarget pTarget) {
            processAjax(pTarget);
            LoginPanel.this.onSubmit.call(pTarget);
        }

        private void processAjax(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getUserId());
            pTarget.add(getPassword());
        }
    }

}

/**
 * 
 */
package com.jabaraster.petshop.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.beaneditor.BeanEditor;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.string.StringValueConversionException;

import com.jabaraster.petshop.entity.ELoginPassword_;
import com.jabaraster.petshop.entity.EUser;
import com.jabaraster.petshop.entity.EUser_;
import com.jabaraster.petshop.model.Duplicate;
import com.jabaraster.petshop.service.IUserService;
import com.jabaraster.petshop.web.ui.component.BodyCssHeaderItem;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public abstract class UserEditPageBase extends RestrictedPageBase {
    private static final long                        serialVersionUID               = 7454930682959012116L;

    private static final CssResourceReference        CSS_REF_VALIDATE_ENGINE        = new CssResourceReference(PetListPage.class,
                                                                                            "validationEngine/css/validationEngine.jquery.css"); //$NON-NLS-1$
    private static final JavaScriptResourceReference JS_REF_VALIDATE_ENGINE         = new JavaScriptResourceReference(PetListPage.class,
                                                                                            "validationEngine/js/jquery.validationEngine.js");   //$NON-NLS-1$
    private static final JavaScriptResourceReference JS_REF_VALIDATE_ENGINE_MESSAGE = new JavaScriptResourceReference(PetListPage.class,
                                                                                            "validationEngine/js/jquery.validationEngine-ja.js"); //$NON-NLS-1$
    private static final CssResourceReference        CSS_REF_BOOTSTRAP_SWITCH       = new CssResourceReference(PetListPage.class,
                                                                                            "bootstrapSwitch/css/bootstrap-switch.min.css");     //$NON-NLS-1$
    private static final JavaScriptResourceReference JS_REF_BOOTSTRAP_SWITCH        = new JavaScriptResourceReference(PetListPage.class,
                                                                                            "bootstrapSwitch/js/bootstrap-switch.min.js");       //$NON-NLS-1$

    /**
     * 
     */
    protected final EUser                            userValue;

    private final PasswordValue                      passwordValue                  = new PasswordValue();

    @Inject
    IUserService                                     userService;

    private final Handler                            handler                        = new Handler();

    private FeedbackPanel                            feedback;

    private Form<?>                                  form;
    private BeanEditor<EUser>                        editor;
    private PasswordTextField                        password;
    private FeedbackPanel                            passwordFeedback;
    private PasswordTextField                        passwordConfirmation;
    private FeedbackPanel                            passwordConfirmationFeedback;
    private AjaxButton                               submitter;

    /**
     * 
     */
    public UserEditPageBase() {
        this(new EUser());
    }

    /**
     * @param pParameters -
     */
    public UserEditPageBase(final PageParameters pParameters) {
        super(pParameters);
        try {
            this.userValue = this.userService.findById(pParameters.get(0).toLong());
            initialize();
        } catch (StringValueConversionException | NotFound e) {
            throw new RestartResponseException(WebApplication.get().getHomePage());
        }
    }

    /**
     * @param pUser -
     */
    private UserEditPageBase(final EUser pUser) {
        this.userValue = pUser;
        initialize();
    }

    /**
     * @see com.jabaraster.petshop.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.render(BodyCssHeaderItem.get());

        pResponse.render(ComponentCssHeaderItem.forType(UserEditPageBase.class));

        pResponse.render(CssHeaderItem.forReference(CSS_REF_BOOTSTRAP_SWITCH));
        pResponse.render(JavaScriptHeaderItem.forReference(JS_REF_BOOTSTRAP_SWITCH));

        pResponse.render(CssHeaderItem.forReference(CSS_REF_VALIDATE_ENGINE));
        pResponse.render(JavaScriptHeaderItem.forReference(JS_REF_VALIDATE_ENGINE));
        pResponse.render(JavaScriptHeaderItem.forReference(JS_REF_VALIDATE_ENGINE_MESSAGE));

        pResponse.render(ComponentJavaScriptHeaderItem.minimizedForType(UserEditPageBase.class));
        try {
            JavaScriptUtil.addFocusScript(pResponse, getEditor().findInputComponent(EUser_.userId.getName()).getFirstFormComponent());
        } catch (final NotFound e) {
            // 処理なし
        }
    }

    /**
     * @see org.apache.wicket.Page#onBeforeRender()
     */
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }

    @SuppressWarnings("serial")
    private BeanEditor<EUser> getEditor() {
        if (this.editor == null) {
            this.editor = new BeanEditor<EUser>("editor", this.userValue) { //$NON-NLS-1$
                @Override
                protected void onBeforeRender() {
                    super.onBeforeRender();
                    try {
                        final FormComponent<?> input = findInputComponent(EUser_.userId.getName()).getFirstFormComponent();
                        input.add(AttributeModifier.append("class", "validate[required]")); //$NON-NLS-1$//$NON-NLS-2$
                    } catch (final NotFound e) {
                        throw ExceptionUtil.rethrow(e);
                    }
                }
            };
        }
        return this.editor;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new ComponentFeedbackPanel("feedback", this); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getEditor());
            this.form.add(getPassword());
            this.form.add(getPasswordFeedback());
            this.form.add(getPasswordConfirmation());
            this.form.add(getPasswordConfirmationFeedback());
            this.form.add(getSubmitter());
            this.form.add(new EqualPasswordInputValidator(getPassword(), getPasswordConfirmation()));
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            final String s = ELoginPassword_.password.getName();
            this.password = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.password;
    }

    private PasswordTextField getPasswordConfirmation() {
        if (this.passwordConfirmation == null) {
            final String s = ELoginPassword_.password.getName() + "Confirmation"; //$NON-NLS-1$
            this.passwordConfirmation = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.passwordConfirmation;
    }

    private FeedbackPanel getPasswordConfirmationFeedback() {
        if (this.passwordConfirmationFeedback == null) {
            this.passwordConfirmationFeedback = new ComponentFeedbackPanel(getPasswordConfirmation().getId() + "Feedback", getPasswordConfirmation()); //$NON-NLS-1$
        }
        return this.passwordConfirmationFeedback;
    }

    private FeedbackPanel getPasswordFeedback() {
        if (this.passwordFeedback == null) {
            this.passwordFeedback = new ComponentFeedbackPanel(getPassword().getId() + "Feedback", getPassword()); //$NON-NLS-1$
        }
        return this.passwordFeedback;
    }

    @SuppressWarnings("serial")
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserEditPageBase.this.handler.onError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserEditPageBase.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private void initialize() {
        this.add(getForm());
    }

    /**
     * @param pUser -
     * @return -
     */
    public static PageParameters createParameters(final EUser pUser) {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        if (!pUser.isPersisted()) {
            throw new IllegalArgumentException("永続化されていないエンティティは処理出来ません."); //$NON-NLS-1$
        }
        return createParameters(pUser.getId().longValue());
    }

    /**
     * @param pEUserId -
     * @return -
     */
    public static PageParameters createParameters(final long pEUserId) {
        final PageParameters ret = new PageParameters();
        ret.set(0, Long.valueOf(pEUserId));
        return ret;
    }

    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 6149418547207914836L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender();

        private void onError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getForm());
        }

        private void onSubmit(final AjaxRequestTarget pTarget) {
            try {
                UserEditPageBase.this.userService.insert(UserEditPageBase.this.userValue, getPassword().getModelObject());
                setResponsePage(UserListPage.class);
            } catch (final Duplicate e) {
                error("ユーザIDは既に使われています."); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getForm());
                pTarget.add(getForm());
            }
        }
    }

    @SuppressWarnings("unused")
    private static class PasswordValue implements Serializable {
        private static final long serialVersionUID = 8620839379550673825L;

        private String            password;
        private String            passwordConfirmation;

        /**
         * @return the password
         */
        public String getPassword() {
            return this.password;
        }

        /**
         * @return the passwordConfirmation
         */
        public String getPasswordConfirmation() {
            return this.passwordConfirmation;
        }

        /**
         * @param pPassword the password to set
         */
        public void setPassword(final String pPassword) {
            this.password = pPassword;
        }

        /**
         * @param pPasswordConfirmation the passwordConfirmation to set
         */
        public void setPasswordConfirmation(final String pPasswordConfirmation) {
            this.passwordConfirmation = pPasswordConfirmation;
        }
    }
}

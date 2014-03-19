package com.jabaraster.petshop.web.ui;

import jabara.general.ArgUtil;
import jabara.wicket.LoginPageInstantiationAuthorizer;
import jabara.wicket.MarkupIdForceOutputer;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.time.Duration;

import com.google.inject.Injector;
import com.jabaraster.petshop.web.ui.page.AdministrationPageBase;
import com.jabaraster.petshop.web.ui.page.LoginPage;
import com.jabaraster.petshop.web.ui.page.LogoutPage;
import com.jabaraster.petshop.web.ui.page.PetDeletePage;
import com.jabaraster.petshop.web.ui.page.PetEditPage;
import com.jabaraster.petshop.web.ui.page.PetListPage;
import com.jabaraster.petshop.web.ui.page.PetNewPage;
import com.jabaraster.petshop.web.ui.page.RestrictedPageBase;
import com.jabaraster.petshop.web.ui.page.UserDeletePage;
import com.jabaraster.petshop.web.ui.page.UserEditPage;
import com.jabaraster.petshop.web.ui.page.UserListPage;
import com.jabaraster.petshop.web.ui.page.UserNewPage;
import com.jabaraster.petshop.web.ui.page.WebPageBase;

/**
 *
 */
public class WicketApplication extends WebApplication {

    private static final String       ENC    = "UTF-8";  //$NON-NLS-1$

    private static List<MenuInfo>     _menus = Collections.unmodifiableList(Arrays.asList(new MenuInfo[] { //
                                                     new MenuInfo(PetNewPage.class, Models.readOnly("ペット登録")) // //$NON-NLS-1$
            , new MenuInfo(UserListPage.class, Models.readOnly("ユーザ一覧")) // //$NON-NLS-1$
                                                     }));

    private final IProvider<Injector> injectorProvider;

    /**
     * @param pInjectorProvider Guiceの{@link Injector}を供給するオブジェクト. DI設定に使用します.
     */
    public WicketApplication(final IProvider<Injector> pInjectorProvider) {
        ArgUtil.checkNull(pInjectorProvider, "pInjectorProvider"); //$NON-NLS-1$
        this.injectorProvider = pInjectorProvider;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return PetListPage.class;
    }

    /**
     * {@link HttpSession}を取得するメソッドをどこに持たせるかは、非常に悩む. <br>
     * 
     * @return -
     */
    @SuppressWarnings("static-method")
    public HttpSession getHttpSession() {
        return ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
    }

    /**
     * @return -
     */
    public Injector getInjector() {
        return this.injectorProvider.get();
    }

    /**
     * @return -
     */
    @SuppressWarnings("static-method")
    public List<MenuInfo> getMenus() {
        return _menus;
    }

    /**
     * @return -
     */
    @SuppressWarnings("static-method")
    public Class<? extends WebPage> getPetListPage() {
        return PetListPage.class;
    }

    /**
     * @param pResource -
     * @return -
     */
    @SuppressWarnings("static-method")
    public ResourceReference getSharedResourceReference(final Resource pResource) {
        ArgUtil.checkNull(pResource, "pResource"); //$NON-NLS-1$
        return new SharedResourceReference(pResource.getName());
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(final Request pRequest, @SuppressWarnings("unused") final Response pResponse) {
        return new AppSession(pRequest);
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountResources();
        mountPages();
        initializeEncoding();
        initializeInjection();
        initializeSecurity();
        initializeOther();
    }

    private void initializeEncoding() {
        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }

    private void initializeInjection() {
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, this.injectorProvider.get()));
    }

    private void initializeOther() {
        getComponentInstantiationListeners().add(new MarkupIdForceOutputer());
    }

    private void initializeSecurity() {
        getSecuritySettings().setAuthorizationStrategy(new LoginPageInstantiationAuthorizer() {

            @Override
            protected Class<? extends Page> getFirstPageType() {
                return PetListPage.class;
            }

            @Override
            protected Class<? extends Page> getLoginPageType() {
                return LoginPage.class;
            }

            @Override
            protected Class<? extends Page> getRestictedPageType() {
                return RestrictedPageBase.class;
            }

            @Override
            protected boolean isAuthenticated() {
                final AppSession session = AppSession.get();
                return session.isAuthenticated();
            }

            @Override
            protected boolean isPermittedPage(final Class<? extends WebPage> pPageType) {
                if (AppSession.get().getLoginUser().isAdministrator()) {
                    return true;
                }
                return !AdministrationPageBase.class.isAssignableFrom(pPageType);
            }
        });
    }

    @SuppressWarnings("nls")
    private void mountPages() {
        this.mountPage("login", LoginPage.class);
        this.mountPage("logout", LogoutPage.class);

        this.mountPage("pet/", PetListPage.class);
        this.mountPage("pet/index", PetListPage.class);
        this.mountPage("pet/new", PetNewPage.class);
        this.mountPage("pet/edit", PetEditPage.class);
        this.mountPage("pet/delete", PetDeletePage.class);

        this.mountPage("user/", UserListPage.class);
        this.mountPage("user/index", UserListPage.class);
        this.mountPage("user/new", UserNewPage.class);
        this.mountPage("user/edit", UserEditPage.class);
        this.mountPage("user/delete", UserDeletePage.class);
    }

    private void mountResource(final Resource pResource, final String pFilePath, final Duration pCacheDuration) {
        mountResource(pResource.getName(), new ResourceReference(pResource.getName()) {
            private static final long serialVersionUID = -8982729375012083247L;

            @SuppressWarnings("resource")
            @Override
            public IResource getResource() {
                return new ResourceStreamResource(new UrlResourceStream(WicketApplication.class.getResource(pFilePath))) //
                        .setCacheDuration(pCacheDuration) //
                ;
            }
        });
    }

    @SuppressWarnings({ "nls" })
    private void mountResources() {
        mountResource(Resource.BACK, "brickwall.png", Duration.days(10));
        mountResource(Resource.FAVICON, "favicon.png", Duration.days(10));
    }

    /**
     * @return -
     */
    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }

    /**
     * @author jabaraster
     */
    public static class MenuInfo implements Serializable {
        private static final long                  serialVersionUID = 1620487003317541303L;

        private final IModel<String>               titleModel;
        private final Class<? extends WebPageBase> pageType;

        MenuInfo(final Class<? extends WebPageBase> pPageType, final IModel<String> pTitleModel) {
            this.titleModel = pTitleModel;
            this.pageType = pPageType;
        }

        /**
         * @return pageTypeを返す.
         */
        public Class<? extends WebPageBase> getPageType() {
            return this.pageType;
        }

        /**
         * @return titleModelを返す.
         */
        public IModel<String> getTitleModel() {
            return this.titleModel;
        }

    }

    /**
     * @author jabaraster
     */
    public enum Resource {
        /**
         * 
         */
        BACK("back"), //$NON-NLS-1$

        /**
         * 
         */
        FAVICON("favicon"), //$NON-NLS-1$

        ;

        private final String name;

        Resource(final String pName) {
            this.name = pName;
        }

        /**
         * @return -
         */
        public String getName() {
            return this.name;
        }
    }
}

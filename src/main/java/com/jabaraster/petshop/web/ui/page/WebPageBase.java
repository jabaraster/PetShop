package com.jabaraster.petshop.web.ui.page;

import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.IconHeaderItem;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.jabaraster.petshop.Environment;
import com.jabaraster.petshop.web.ui.AppSession;
import com.jabaraster.petshop.web.ui.WicketApplication;
import com.jabaraster.petshop.web.ui.WicketApplication.MenuInfo;
import com.jabaraster.petshop.web.ui.WicketApplication.Resource;
import com.jabaraster.petshop.web.ui.component.BodyCssHeaderItem;

/**
 *
 */
public abstract class WebPageBase extends WebPage {
    private static final long                        serialVersionUID  = -7105185519134177245L;

    private static final CssResourceReference        REF_BOOTSTRAP_CSS = new CssResourceReference(WebPageBase.class, "bootstrap/css/bootstrap.css"); //$NON-NLS-1$
    private static final CssResourceReference        REF_APP_CSS       = new CssResourceReference(WebPageBase.class, "App.css");                    //$NON-NLS-1$
    private static final JavaScriptResourceReference REF_BOOTSTRAP_JS  = new JavaScriptResourceReference(WebPageBase.class,
                                                                               "bootstrap/js/bootstrap.js");                                        //$NON-NLS-1$

    private Label                                    titleLabel;
    private Label                                    applicationName;
    private Link<?>                                  goTop;

    private ListView<MenuInfo>                       leftAlignMenus;
    private Panel                                    rightAlignPanel;

    /**
     * 
     */
    protected WebPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected WebPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(getTitleLabel());
        this.add(getGoTop());
        this.add(getLeftAlignMenus());
        this.add(getRightAlignPanel());
    }

    /**
     * {@link HttpSession}を取得するメソッドをどこに持たせるかは、非常に悩む. <br>
     * staticユーティリティメソッドにすることも出来るのだが、それだとServiceクラスの中から使えてしまう. <br>
     * ここでは、あくまでHTTPを意識するのはView層(=Wicket)まで、ということを主張するために、Wicketのクラスに持たせることにした. <br>
     * 
     * @return -
     */
    @SuppressWarnings("static-method")
    public HttpSession getHttpSession() {
        return ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
    }

    /**
     * @return -
     * @see org.apache.wicket.Component#getApplication()
     */
    public WicketApplication getPetShopApplication() {
        return (WicketApplication) super.getApplication();
    }

    /**
     * @see org.apache.wicket.Component#getSession()
     */
    @Override
    public AppSession getSession() {
        return (AppSession) super.getSession();
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.render(IconHeaderItem.forReference(WicketApplication.get().getSharedResourceReference(Resource.FAVICON)));

        pResponse.render(BodyCssHeaderItem.get());
        pResponse.render(CssHeaderItem.forReference(REF_BOOTSTRAP_CSS));
        pResponse.render(CssHeaderItem.forReference(REF_APP_CSS));

        pResponse.render(JavaScriptHeaderItem.forReference(JavaScriptUtil.JQUERY_1_9_1_REFERENCE));
        pResponse.render(JavaScriptHeaderItem.forReference(REF_BOOTSTRAP_JS));

        pResponse.render(ComponentCssHeaderItem.forType(WebPageBase.class));
    }

    /**
     * 右寄せメニューとなるパネルを生成して下さい.
     * 
     * @param pId -
     * @return -
     */
    protected abstract Panel createRightAlignComponent(final String pId);

    /**
     * @return -
     */
    protected abstract IModel<? extends List<? extends MenuInfo>> getLeftAlighMenusModel();

    /**
     * @return 右寄せメニューとなるパネル.
     */
    protected Panel getRightAlignPanel() {
        if (this.rightAlignPanel == null) {
            this.rightAlignPanel = createRightAlignComponent("rightAlignComponent"); //$NON-NLS-1$
        }
        return this.rightAlignPanel;
    }

    /**
     * titleタグの中を表示するラベルです. <br>
     * このメソッドはサブクラスでコンポーネントIDの重複を避けるためにprotectedにしています. <br>
     * 
     * @return titleタグの中を表示するラベル.
     */
    @SuppressWarnings({ "nls" })
    protected Label getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new Label("titleLabel", Models.of(getTitleLabelModel().getObject() + " - " + Environment.getApplicationName()));
        }
        return this.titleLabel;
    }

    /**
     * @return HTMLのtitleタグの内容
     */
    protected abstract IModel<String> getTitleLabelModel();

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
    private ListView<MenuInfo> getLeftAlignMenus() {
        if (this.leftAlignMenus == null) {
            this.leftAlignMenus = new ListView<MenuInfo>("leftAlignMenus", getLeftAlighMenusModel()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<MenuInfo> pItem) {
                    final MenuInfo menu = pItem.getModelObject();
                    final Link<?> goPage = new BookmarkablePageLink<>("goPage", menu.getPageType()); //$NON-NLS-1$
                    goPage.add(new Label("label", menu.getTitleModel())); //$NON-NLS-1$
                    pItem.add(goPage);
                }
            };
        }
        return this.leftAlignMenus;
    }
}

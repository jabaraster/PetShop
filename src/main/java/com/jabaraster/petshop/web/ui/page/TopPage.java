package com.jabaraster.petshop.web.ui.page;

import com.jabaraster.petshop.Environment;

import jabara.wicket.Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 *
 */
@SuppressWarnings("synthetic-access")
public class TopPage extends RestrictedPageBase {

    private final Handler     handler          = new Handler();

    private Label             applicationName;
    private Label             now;
    private AjaxLink<?>       reloader;

    /**
     * 
     */
    public TopPage() {
        this.add(getApplicationName());
        this.add(getNow());
        this.add(getReloader());
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("Top"); //$NON-NLS-1$
    }

    private Label getApplicationName() {
        if (this.applicationName == null) {
            this.applicationName = new Label("applicationName", Environment.getApplicationName()); //$NON-NLS-1$
        }
        return this.applicationName;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Label getNow() {
        if (this.now == null) {
            this.now = new Label("now", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()); //$NON-NLS-1$
                }
            });
        }
        return this.now;
    }

    @SuppressWarnings("serial")
    private AjaxLink<?> getReloader() {
        if (this.reloader == null) {
            this.reloader = new IndicatingAjaxLink<Object>("reloader") { //$NON-NLS-1$
                @Override
                public void onClick(final AjaxRequestTarget pTarget) {
                    TopPage.this.handler.onReloaderClick(pTarget);
                }
            };
        }
        return this.reloader;
    }

    private class Handler implements Serializable {

        void onReloaderClick(final AjaxRequestTarget pTarget) {
            pTarget.add(getNow());
        }
    }
}

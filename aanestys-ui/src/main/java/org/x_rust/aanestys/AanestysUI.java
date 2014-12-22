package org.x_rust.aanestys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.x_rust.aanestys.samples.MainScreen;
import org.x_rust.aanestys.samples.authentication.AccessControl;
import org.x_rust.aanestys.samples.authentication.BasicAccessControl;
import org.x_rust.aanestys.samples.authentication.LoginScreen;
import org.x_rust.aanestys.samples.authentication.LoginScreen.LoginListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * 
 */
@Theme("aanestys")
//@Widgetset("org.x_rust.aanestys.AanestysWidgetset")
@CDIUI("")
public class AanestysUI extends UI {

    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("X-Rust vuoden* Äänestys");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        setContent(new MainScreen(AanestysUI.this));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static AanestysUI get() {
        return (AanestysUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

//    @WebServlet(urlPatterns = "/*", name = "AanestysUIServlet", asyncSupported = true)
//    @VaadinServletConfiguration(ui = AanestysUI.class, productionMode = false)
//    public static class AanestysUIServlet extends VaadinServlet {
//        @Override
//        protected void servletInitialized() throws ServletException {
//            super.servletInitialized();
//            /*
//             * Configure the viewport meta tags appropriately on mobile devices.
//             * Instead of device based scaling (default), using responsive
//             * layouts.
//             * 
//             * If using Vaadin TouchKit, this is done automatically and it is
//             * sufficient to have an empty servlet class extending
//             * TouchKitServlet.
//             */
//            getService().addSessionInitListener(
//                    new ViewPortSessionInitListener());
//        }
//    }
}

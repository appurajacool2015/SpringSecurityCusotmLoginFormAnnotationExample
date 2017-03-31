package com.websystique.springsecurity.configuration;

import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;

import java.util.EnumSet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

        rootContext.setDisplayName("SpringSecurityCusotmLoginFormAnnotationExample");
        rootContext.register(WebConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
        DelegatingFilterProxy fp = new DelegatingFilterProxy("springSecurityFilterChain", rootContext);
        FilterRegistration.Dynamic filter = container.addFilter("springSecurityFilterChain", fp);
        filter.addMappingForUrlPatterns(EnumSet.of(REQUEST, ERROR), false, "/*");

    }

}

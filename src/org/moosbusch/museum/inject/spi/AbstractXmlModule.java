/*
 *
 *
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.AbstractModule;
import org.moosbusch.museum.inject.XmlModule;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractXmlModule extends AbstractModule
        implements XmlModule {

    private String language;

    public AbstractXmlModule() {
        this.language = initLanguage();
    }

    protected abstract String createLanguage();

    protected abstract void configureImpl();

    @Override
    protected final void configure() {
        binder().disableCircularProxies();
        configureImpl();
    }

    private String initLanguage() {
        return createLanguage();
    }

    @Override
    public String getLanguage() {
        return language;
    }

}

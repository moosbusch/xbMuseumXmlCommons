/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlModule;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlModule extends AbstractModule implements MuseumXmlModule {

    private String language;
    private Injector defaultInjector;

    public AbstractMuseumXmlModule() {
        this.language = initLanguage();
    }

    protected Injector getDefaultInjector() {
        synchronized (this) {
            if (defaultInjector == null) {
                defaultInjector = Guice.createInjector(this);
            }

            return defaultInjector;
        }
    }

    protected <T extends XmlObject> T injectMembers(T entity) {
        getDefaultInjector().injectMembers(entity);
        return entityCreatedImpl(entity);
    }

    protected <T extends XmlObject> T entityCreatedImpl(T entity) {
        return entity;
    }

    private String initLanguage() {
        return createLanguage();
    }

    protected abstract String createLanguage();

    @Override
    public String getLanguage() {
        return language;
    }
}

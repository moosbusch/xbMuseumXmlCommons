/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.AbstractModule;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlModule;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlModule extends AbstractModule
        implements MuseumXmlModule {

    private final MuseumXmlObjectFactory<? extends MuseumXmlModule, ? extends XmlObject> objectFactory;
    private String language;

    public AbstractMuseumXmlModule(MuseumXmlObjectFactory<? extends MuseumXmlModule,
            ? extends XmlObject> objFactory) {
        this.objectFactory = objFactory;
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

    @Override
    public MuseumXmlObjectFactory<? extends MuseumXmlModule, ? extends XmlObject> getObjectFactory() {
        return objectFactory;
    }

}

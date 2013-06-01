/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject.spi;

import org.moosbusch.museum.inject.impl.XmlInjectorImpl;
import com.google.inject.Guice;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.XmlInjector;
import org.moosbusch.museum.inject.MuseumXmlModule;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;
import org.moosbusch.museum.inject.evt.XmlInjectionListener;
import org.moosbusch.museum.inject.evt.XmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlObjectFactory<T extends MuseumXmlModule, V extends XmlObject>
        implements MuseumXmlObjectFactory<T, V> {

    private XmlInjector injector;
    private T module;

    protected abstract T createModule();

    @Override
    public XmlInjector getInjector() {
        if (this.injector == null) {
            this.injector = new XmlInjectorImpl(
                    Guice.createInjector(getModule()));
        }

        return injector;
    }

    @Override
    public T getModule() {
        if (this.module == null) {
            this.module = createModule();
        }

        return module;
    }

    @Override
    public <X extends XmlObject> void injectChildMembers(X entity) {
        getInjector().injectMembers(entity);
    }

    @Override
    public final <X extends XmlObject> X createTypedObject(Class<X> type) {
        return getInjector().getInstance(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Object createObject(Class type) {
        return createTypedObject(type);
    }

    @Override
    public void addInjectionListener(XmlInjectionListener l) {
        getInjector().addInjectionListener(l);
    }

    @Override
    public void removeInjectionListener(XmlInjectionListener l) {
        getInjector().removeInjectionListener(l);
    }

    @Override
    public void addXmlPostProcessor(XmlPostProcessor p) {
        getInjector().addXmlPostProcessor(p);
    }

    @Override
    public void removeXmlPostProcessor(XmlPostProcessor p) {
        getInjector().removeXmlPostProcessor(p);
    }
}

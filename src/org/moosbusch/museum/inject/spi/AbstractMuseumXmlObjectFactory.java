/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlModule;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlObjectFactory<T extends MuseumXmlModule, V extends XmlObject>
        implements MuseumXmlObjectFactory<T, V> {

    private Injector injector;
    private T module;

    protected abstract T createModule();

    @Override
    public final Injector getInjector() {
        synchronized (this) {
            if (this.injector == null) {
                this.injector = Guice.createInjector(getModule());
            }

            return injector;
        }
    }

    @Override
    public final T getModule() {
        synchronized (this) {
            if (this.module == null) {
                this.module = createModule();
            }

            return module;
        }
    }

    @Override
    public final <X extends XmlObject> void injectChildMembers(X entity) {
        final Object monitor = entity.monitor();

        synchronized (monitor) {
            getInjector().injectMembers(entity);
        }
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
    public <X extends XmlObject> void afterInjectedChildMembers(X entity) {
    }

}

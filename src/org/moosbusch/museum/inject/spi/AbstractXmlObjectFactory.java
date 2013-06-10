/*
 *
 *
 */
package org.moosbusch.museum.inject.spi;

import org.moosbusch.museum.inject.impl.XmlInjectorImpl;
import com.google.inject.Guice;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.XmlInjector;
import org.moosbusch.museum.inject.XmlModule;
import org.moosbusch.museum.inject.XmlObjectFactory;
import org.moosbusch.museum.inject.XmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractXmlObjectFactory<T extends XmlModule, V extends XmlObject>
        implements XmlObjectFactory<T, V> {

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
    public <X extends XmlObject> void injectChildMembers(X injectee) {
        getInjector().injectMembers(injectee);
    }

    @Override
    public final <X extends XmlObject> X createTypedObject(Class<X> type) {
        X result = getInjector().getInstance(type);
        injectChildMembers(result);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Object createObject(Class type) {
        return createTypedObject(type);
    }

    @Override
    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass,
        XmlPostProcessor<? extends XmlObject> p) {
        getInjector().registerXmlPostProcessor(targetClass, p);
    }

    @Override
    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass) {
        getInjector().unregisterXmlPostProcessor(targetClass);
    }
}

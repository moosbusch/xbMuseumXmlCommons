/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlObjectFactory<T extends Module, V extends XmlObject>
    implements MuseumXmlObjectFactory<T, V>{

    public static final String XML_SCHEMA_NS_URI =
            "http://www.w3.org/2001/XMLSchema-instance";
    public static final String SCHEMA_LOCATION_ATTR = "schemaLocation";
    private Injector injector;
    private T module;

    protected abstract T createModule();

    @Override
    public final Injector getInjector() {
        if (this.injector == null) {
            this.injector = Guice.createInjector(getModule());
        }

        return injector;
    }

    public final T getModule() {
        if (this.module == null) {
            this.module = createModule();
        }

        return module;
    }

    @Override
    public <X extends XmlObject> X createTypedObject(Class<X> type) {
        return getInjector().getInstance(type);
    }

    @Override
    public final Object createObject(Class type) {
        return createTypedObject(type);
    }


}

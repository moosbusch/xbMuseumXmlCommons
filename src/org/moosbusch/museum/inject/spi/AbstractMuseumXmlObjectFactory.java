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

    @Override
    public Injector getInjector() {
        return Guice.createInjector(getModule());
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

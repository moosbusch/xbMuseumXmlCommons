/*
 *
 *
 */
package org.moosbusch.museum.inject;

import org.apache.xmlbeans.ObjectFactory;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.evt.XmlInjectionListener;
import org.moosbusch.museum.inject.evt.XmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlObjectFactory<M extends MuseumXmlModule, R extends XmlObject>
        extends ObjectFactory {

    public M getModule();

    public XmlInjector getInjector();

    public <X extends XmlObject> void injectChildMembers(X entity);

    public <X extends XmlObject> X createTypedObject(Class<X> type);

    public R createRootElement();

    public void addInjectionListener(XmlInjectionListener l);

    public void removeInjectionListener(XmlInjectionListener l);

    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass, XmlPostProcessor p);

    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass);
}

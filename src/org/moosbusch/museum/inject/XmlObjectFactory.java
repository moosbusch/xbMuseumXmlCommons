/*
 *
 *
 */
package org.moosbusch.museum.inject;

import org.apache.xmlbeans.ObjectFactory;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface XmlObjectFactory<M extends XmlModule, R extends XmlObject>
        extends ObjectFactory {

    public M getModule();

    public XmlInjector getInjector();

    public <X extends XmlObject> void injectChildMembers(X entity);

    public <X extends XmlObject> X createTypedObject(Class<X> type);

    public R createRootElement();

    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass,
            XmlPostProcessor<? extends XmlObject> p);

    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass);
}

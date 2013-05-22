/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject;

import com.google.inject.Injector;
import org.apache.xmlbeans.ObjectFactory;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlObjectFactory<M extends MuseumXmlModule, R extends XmlObject>
        extends ObjectFactory {

    public M getModule();

    public Injector getInjector();

    public <X extends XmlObject> void injectChildMembers(X entity);

    public <X extends XmlObject> void afterInjectedChildMembers(X entity);

    public <X extends XmlObject> X createTypedObject(Class<X> type);

    public R createRootElement();

}

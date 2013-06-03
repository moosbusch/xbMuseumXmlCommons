/*
 *
 *
 */
package org.moosbusch.museum.inject;

import com.google.inject.Injector;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.evt.XmlInjectionListener;
import org.moosbusch.museum.inject.evt.XmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public interface XmlInjector extends Injector {

    public static final String SET_METHOD_PREFIX = "set";
    public static final String ARRAY_METHOD_SUFFIX = "Array";

    public void addInjectionListener(XmlInjectionListener e);

    public void removeInjectionListener(XmlInjectionListener o);

    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass, XmlPostProcessor p);

    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass);

}

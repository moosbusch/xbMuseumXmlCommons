/*
 *
 *
 */
package org.moosbusch.museum.inject;

import com.google.inject.Injector;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface XmlInjector extends Injector {

    public static final String SET_METHOD_PREFIX = "set";
    public static final String ARRAY_METHOD_SUFFIX = "Array";
    public static final String LIST_METHOD_SUFFIX = "List";

    @Override
    public void injectMembers(Object injectee);

    public void injectXmlMembers(XmlObject injectee);

    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass,
            XmlPostProcessor<? extends XmlObject> p);

    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass);

}

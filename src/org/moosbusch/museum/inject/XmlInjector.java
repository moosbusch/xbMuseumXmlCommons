/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject;

import com.google.inject.Injector;
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

    public void addXmlPostProcessor(XmlPostProcessor p);

    public void removeXmlPostProcessor(XmlPostProcessor p);

}

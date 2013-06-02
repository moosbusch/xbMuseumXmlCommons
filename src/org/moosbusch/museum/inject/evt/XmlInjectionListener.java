/*
 * 
 *
 */
package org.moosbusch.museum.inject.evt;

import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface XmlInjectionListener {

    public void beforeChildMembersInjection(XmlObject injectee);

    public void afterChildMembersInjection(XmlObject injectee);

    public static abstract class Adapter implements XmlInjectionListener {

        @Override
        public void beforeChildMembersInjection(XmlObject injectee) {
        }

        @Override
        public void afterChildMembersInjection(XmlObject injectee) {
        }

    }
}

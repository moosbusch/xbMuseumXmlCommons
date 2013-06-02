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
public interface XmlPostProcessor {

    public void postProcess(XmlObject xmlObject);

    public  Class<? extends XmlObject> getTargetClass();

}

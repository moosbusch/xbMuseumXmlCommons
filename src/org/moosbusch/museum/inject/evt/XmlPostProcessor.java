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
public interface XmlPostProcessor<T extends XmlObject> {

    public void postProcess(T xmlObject);

}

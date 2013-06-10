/*
 *
 *
 */
package org.moosbusch.museum.inject;

import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface XmlPostProcessor<T extends XmlObject> {

    public void postProcess(T xmlObject);

}

/*
 *
 *
 */
package org.moosbusch.museum.inject;

import com.google.inject.Module;
import com.google.inject.Provides;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.document.XmlDocument;

/**
 *
 * @author moosbusch
 */
public interface XmlModule extends Module {

    @Provides
    public XmlDocument<? extends XmlObject, ? extends XmlObjectFactory
            <? extends XmlModule, ? extends XmlObject>,
            ? extends XmlObject, ? extends XmlObject> createDocument();

    public String getLanguage();

}

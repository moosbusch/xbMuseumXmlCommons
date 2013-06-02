/*
 * 
 *
 */
package org.moosbusch.museum.inject;

import com.google.inject.Module;
import com.google.inject.Provides;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.document.MuseumXmlDocument;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlModule extends Module {

    @Provides
    public MuseumXmlDocument<? extends XmlObject, ? extends MuseumXmlObjectFactory
            <? extends MuseumXmlModule, ? extends XmlObject>,
            ? extends XmlObject, ? extends XmlObject> createDocument();

    public MuseumXmlObjectFactory<? extends MuseumXmlModule, ? extends XmlObject> getObjectFactory();

    public String getLanguage();

}

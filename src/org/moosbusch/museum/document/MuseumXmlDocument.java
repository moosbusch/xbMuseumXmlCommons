/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import org.apache.xmlbeans.ObjectFactory;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlDocument<T extends ObjectFactory> {

    public static final String PATH_DELEGATE_INTERFACE_CLASS =
            "org.apache.xmlbeans.impl.xpath.saxon.XBeansXPathSaxon95";
    public static final String DEFAULT_LANGUAGE =
            Locale.getDefault().toLanguageTag();

    public void loadDocument(InputStream input) throws IOException, XmlException;

    public void saveDocument(OutputStream output) throws IOException;

    public void clearDocument();

    public String getLanguage();

    public Object getObjectByXPath(String xpath);

    public T getObjectFactory();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.document;

import org.moosbusch.museum.inject.MuseumXmlObjectFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlModule;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlDocument<T extends XmlObject,
        V extends MuseumXmlObjectFactory<? extends MuseumXmlModule, T>,
        U extends XmlObject, E extends XmlObject> {

    public static final String PATH_DELEGATE_INTERFACE_CLASS =
            "org.apache.xmlbeans.impl.xpath.saxon.XBeansXPathSaxon95";
    public static final String XML_SCHEMA_NS_URI =
            "http://www.w3.org/2001/XMLSchema-instance";
    public static final String SCHEMA_LOCATION_ATTR = "schemaLocation";
    public static final String DEFAULT_LANGUAGE =
            Locale.getDefault().getLanguage();
    public static final String DEFAULT_RELATED_ENCODING = "DC";

    public T getRootWrapperElement();

    public U getRootElement();

    public void clearDocument();

    public void loadDocument(InputStream input) throws IOException, XmlException;

    public void saveDocument(OutputStream output) throws IOException;

    public void addItem(E entry);

    public void removeItem(E entry);

    public Collection<E> getItems();

    public <X extends Object> Collection <X> getObjectsByXPath(String xpath);

    public <X extends XmlObject> X createTypedObject(Class<X> type);

    public V getObjectFactory();
}

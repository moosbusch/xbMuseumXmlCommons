/* 
 * Copyright 2013 Gunnar Kappei.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.moosbusch.museum.document;

import org.moosbusch.museum.inject.XmlObjectFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.XmlModule;

/**
 *
 * @author moosbusch
 */
public interface XmlDocument<T extends XmlObject,
        V extends XmlObjectFactory<? extends XmlModule, T>,
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

    public void addRecord(E record);

    public void removeRecord(E record);

    public Collection<E> getRecords();

    public <X extends Object> Collection <X> getObjectsByXPath(String xpath);

    public <X extends XmlObject> X createTypedObject(Class<X> type);

    public V getObjectFactory();
}

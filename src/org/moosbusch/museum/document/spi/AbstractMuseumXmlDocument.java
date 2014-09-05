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
package org.moosbusch.museum.document.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.store.Path;
import org.moosbusch.museum.document.MuseumXmlDocument;
import org.moosbusch.museum.inject.MuseumXmlModule;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;

/**
 *
 * @author moosbusch
 * @param <T The type of object wrapping the root-element>
 * @param <V The object-factory responsible for creating new objects>
 * @param <U The type representing the root-element>
 * @param <E The object-type for records to be contained within the document>
 */
public abstract class AbstractMuseumXmlDocument<T extends XmlObject, V extends MuseumXmlObjectFactory<? extends MuseumXmlModule, T>, U extends XmlObject, E extends XmlObject>
        implements MuseumXmlDocument<T, V, U, E> {

    private final V objectFactory;
    private T rootWrapper;

    public AbstractMuseumXmlDocument() {
        this.objectFactory = initObjectFactory();
        init();
    }

    public AbstractMuseumXmlDocument(InputStream in) throws IOException, XmlException {
        this.objectFactory = initObjectFactory();
        init(in);
    }

    private void init() {
        clearDocument();
    }

    private void init(InputStream in) throws IOException, XmlException {
        loadDocument(in);
    }

    private V initObjectFactory() {
        return createObjectFactory();
    }

    protected abstract V createObjectFactory();

    protected abstract T loadDocumentImpl(InputStream in) throws IOException, XmlException;

    protected abstract void saveDocumentImpl(T rootElement, OutputStream out) throws IOException;

    @Override
    public void clearDocument() {
        setRootWrapperElement(getObjectFactory().createRootWrapper());
    }

    @Override
    public <X extends Object> Collection<X> getObjectsByXPath(String xpath, XmlOptions... options) {
        XmlObject[] result;
        XmlOptions xmlOptions;

        if (options == null) {
            xmlOptions = new XmlOptions();
        } else {
            xmlOptions = options[0];
        }

        xmlOptions.put(Path.PATH_DELEGATE_INTERFACE, PATH_DELEGATE_INTERFACE_CLASS);
        result = getRootElement().selectPath(xpath, xmlOptions);

        if (result != null) {
            return (Collection<X>) Collections.unmodifiableCollection(Arrays.asList(result));
        }

        return Collections.unmodifiableCollection(Collections.EMPTY_LIST);
    }

    @Override
    public <X> Collection<X> getObjectsByXQuery(String xquery, XmlOptions... options) {
        XmlObject[] result;

        if (options == null) {
            result = getRootElement().execQuery(xquery);
        } else {
            result = getRootElement().execQuery(xquery, options[0]);
        }

        if (result != null) {
            return (Collection<X>) Collections.unmodifiableCollection(Arrays.asList(result));
        }

        return Collections.unmodifiableCollection(Collections.EMPTY_LIST);
    }

    @Override
    public final void loadDocument(InputStream input) throws IOException, XmlException {
        setRootWrapperElement(loadDocumentImpl(input));
        input.close();
    }

    @Override
    public final void saveDocument(OutputStream output) throws IOException {
        saveDocumentImpl(getRootWrapperElement(), output);
        output.flush();
        output.close();
    }

    @Override
    public void addRecord(E record) {
        getRecords().add(record);
    }

    @Override
    public void removeRecord(E record) {
        getRecords().remove(record);
    }

    @Override
    public T getRootWrapperElement() {
        return rootWrapper;
    }

    protected void setRootWrapperElement(T rootWrapperElement) {
        this.rootWrapper = rootWrapperElement;
    }

    @Override
    public <X extends XmlObject> X createTypedObject(Class<X> type) {
        return getObjectFactory().createTypedObject(type);
    }

    @Override
    public V getObjectFactory() {
        return objectFactory;
    }

    @Override
    public String xmlText() {
        return getRootWrapperElement().xmlText();
    }

    @Override
    public String xmlText(XmlOptions options) {
        return getRootWrapperElement().xmlText(options);
    }

}

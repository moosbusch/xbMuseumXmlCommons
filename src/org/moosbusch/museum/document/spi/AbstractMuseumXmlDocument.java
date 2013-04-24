/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.document.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.store.Path;
import org.moosbusch.museum.document.MuseumXmlDocument;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;
import org.moosbusch.museum.inject.MuseumXmlModule;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlDocument<T extends XmlObject,
        V extends MuseumXmlObjectFactory<? extends MuseumXmlModule, T>,
        U extends XmlObject, E extends XmlObject>
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

    @Override
    public void clearDocument() {
        setRootWrapperElement(getObjectFactory().createRootElement());
    }

    @Override
    public <X extends Object> Collection<X> getObjectsByXPath(String xpath) {
        synchronized (getRootWrapperElement().monitor()) {
            XmlOptions options = new XmlOptions();
            options.put(Path.PATH_DELEGATE_INTERFACE, PATH_DELEGATE_INTERFACE_CLASS);
            XmlCursor cur = getRootElement().newCursor();
            cur.selectPath(xpath, options);
            Set<X> result = new LinkedHashSet<X>();

            while (cur.hasNextSelection()) {
                if (cur.toNextSelection()) {
                    result.add((X) cur.getObject());
                }
            }

            cur.dispose();

            return Collections.unmodifiableCollection(result);
        }
    }

    @Override
    public void loadDocument(InputStream input) throws IOException, XmlException {
        synchronized (getRootWrapperElement().monitor()) {
            setRootWrapperElement(getObjectFactory().loadDocument(input));
        }
    }

    @Override
    public void saveDocument(OutputStream output) throws IOException {
        synchronized (getRootWrapperElement().monitor()) {
            getObjectFactory().saveDocument(getRootWrapperElement(), output);
            output.flush();
            output.close();
        }
    }

    public void addItem(E entry) {
        synchronized (getRootWrapperElement().monitor()) {
            getItems().add(entry);
        }
    }

    public void removeItem(E entry) {
        synchronized (getRootWrapperElement().monitor()) {
            getItems().remove(entry);
        }
    }

    @Override
    public T getRootWrapperElement() {
        return rootWrapper;
    }

    protected void setRootWrapperElement(T rootWrapperElement) {
        this.rootWrapper = rootWrapperElement;
    }

    @Override
    public V getObjectFactory() {
        return objectFactory;
    }
}

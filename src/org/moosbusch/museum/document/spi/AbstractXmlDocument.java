/*
 *
 *
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
import org.moosbusch.museum.document.XmlDocument;
import org.moosbusch.museum.inject.XmlObjectFactory;
import org.moosbusch.museum.inject.XmlModule;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractXmlDocument<T extends XmlObject, V extends XmlObjectFactory<? extends XmlModule, T>, U extends XmlObject, E extends XmlObject>
        implements XmlDocument<T, V, U, E> {

    private final V objectFactory;
    private T rootWrapper;

    public AbstractXmlDocument() {
        this.objectFactory = initObjectFactory();
        init();
    }

    public AbstractXmlDocument(InputStream in) throws IOException, XmlException {
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
        setRootWrapperElement(getObjectFactory().createRootElement());
    }

    @Override
    public <X extends Object> Collection<X> getObjectsByXPath(String xpath) {
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
}

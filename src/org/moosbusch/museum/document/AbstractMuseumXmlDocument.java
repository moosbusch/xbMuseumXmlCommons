/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.document;

import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlbeans.ObjectFactory;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlDocument<T extends ObjectFactory> implements MuseumXmlDocument<T> {

    private final String language;
    private final T objectFactory;

    public AbstractMuseumXmlDocument(String language) {
        this.language = initLanguage();
        this.objectFactory = initObjectFactory();
        init();
    }

    public AbstractMuseumXmlDocument(InputStream in, String language) throws IOException, XmlException {
        this.language = initLanguage();
        this.objectFactory = initObjectFactory();
        init(in);
    }

    private void init() {
        clearDocument();
    }

    private void init(InputStream in) throws IOException, XmlException {
        loadDocument(in);
    }

    private String initLanguage() {
        return createLanguage();
    }

    private T initObjectFactory() {
        return createObjectFactory();
    }

    protected abstract T createObjectFactory();

    protected abstract String createLanguage();

    @Override
    public T getObjectFactory() {
        return objectFactory;
    }

    @Override
    public String getLanguage() {
        return language;
    }
}

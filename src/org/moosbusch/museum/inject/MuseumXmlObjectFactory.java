/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject;

import com.google.inject.Injector;
import com.google.inject.Module;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.xmlbeans.ObjectFactory;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlObjectFactory<T extends Module, V extends XmlObject> extends ObjectFactory {

    public T getModule();

    public Injector getInjector();

    public <X extends XmlObject> X createTypedObject(Class<X> type);

    public V createRootElement();

    public V loadDocument(InputStream in)
            throws IOException, XmlException;

    public void saveDocument(V rootElement, OutputStream out) throws IOException;
}

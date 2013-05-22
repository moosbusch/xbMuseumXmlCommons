/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlModule;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlModule extends AbstractModule
        implements MuseumXmlModule {

    private final MuseumXmlObjectFactory<? extends MuseumXmlModule, ? extends XmlObject> objectFactory;
    private String language;

    public AbstractMuseumXmlModule(MuseumXmlObjectFactory<? extends MuseumXmlModule,
            ? extends XmlObject> objFactory) {
        this.objectFactory = objFactory;
        this.language = initLanguage();
    }

    protected abstract String createLanguage();

    protected abstract void configureImpl();

    @Override
    protected final void configure() {
        binder().disableCircularProxies();
        binder().bindListener(Matchers.any(), new InjectionListenerImpl());
        configureImpl();
    }

    private String initLanguage() {
        return createLanguage();
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public MuseumXmlObjectFactory<? extends MuseumXmlModule, ? extends XmlObject> getObjectFactory() {
        return objectFactory;
    }

    private class InjectionListenerImpl implements TypeListener,
            InjectionListener {

        @Override
        public void afterInjection(Object injectee) {
            if (injectee != null) {
                if (injectee instanceof XmlObject) {
                    XmlObject currentInjectee = (XmlObject) injectee;

                    synchronized (currentInjectee.monitor()) {
                        getObjectFactory().afterInjectedChildMembers(currentInjectee);
                    }
                }
            }
        }

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            if (XmlObject.class.isAssignableFrom(type.getRawType())) {
                encounter.register(InjectionListenerImpl.this);
            }
        }
    }
}

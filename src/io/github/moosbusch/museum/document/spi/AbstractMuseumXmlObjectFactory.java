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
package io.github.moosbusch.museum.document.spi;

import com.google.inject.Guice;
import java.util.Objects;
import org.apache.xmlbeans.XmlObject;
import io.github.moosbusch.museum.document.MuseumXmlObjectFactory;
import io.github.moosbusch.museum.inject.MuseumXmlInjector;
import io.github.moosbusch.museum.inject.MuseumXmlModule;
import io.github.moosbusch.museum.inject.MuseumXmlPostProcessor;
import io.github.moosbusch.museum.inject.impl.DefaultMuseumXmlInjector;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlObjectFactory<T extends MuseumXmlModule, V extends XmlObject>
        implements MuseumXmlObjectFactory<T, V> {

    private final MuseumXmlInjector injector;
    private final T module;

    public AbstractMuseumXmlObjectFactory() {
        this.module = initModule();
        this.injector = initInjector();
    }

    private T initModule() {
        return Objects.requireNonNull(createModule());
    }

    private MuseumXmlInjector initInjector() {
        return Objects.requireNonNull(createInjector());
    }

    protected abstract T createModule();

    protected MuseumXmlInjector createInjector() {
        return new DefaultMuseumXmlInjector(
                    Guice.createInjector(getModule()));
    }

    @Override
    public MuseumXmlInjector getInjector() {
        return injector;
    }

    @Override
    public T getModule() {
        return module;
    }

    @Override
    public <X extends XmlObject> void injectChildMembers(X injectee) {
        getInjector().injectMembers(injectee);
    }

    @Override
    public final <X extends XmlObject> X createTypedObject(Class<X> type) {
        X result = null;

        try {
            result = getInjector().getInstance(type);
        } finally {
            if (result != null) {
                injectChildMembers(result);
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Object createObject(Class type) {
        return Objects.requireNonNull(createTypedObject(type));
    }

    @Override
    public void registerXmlPostProcessor(MuseumXmlPostProcessor p) {
        getInjector().registerXmlPostProcessor(p);
    }

    @Override
    public void unregisterXmlPostProcessor(MuseumXmlPostProcessor p) {
        getInjector().unregisterXmlPostProcessor(p);
    }

}

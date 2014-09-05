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
package org.moosbusch.museum.inject.spi;

import org.moosbusch.museum.inject.impl.DefaultMuseumXmlInjector;
import com.google.inject.Guice;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlInjector;
import org.moosbusch.museum.inject.MuseumXmlModule;
import org.moosbusch.museum.inject.MuseumXmlObjectFactory;
import org.moosbusch.museum.inject.MuseumXmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlObjectFactory<T extends MuseumXmlModule, V extends XmlObject>
        implements MuseumXmlObjectFactory<T, V> {

    private MuseumXmlInjector injector;
    private T module;

    protected abstract T createModule();

    @Override
    public MuseumXmlInjector getInjector() {
        if (this.injector == null) {
            this.injector = new DefaultMuseumXmlInjector(
                    Guice.createInjector(getModule()));
        }

        return injector;
    }

    @Override
    public T getModule() {
        if (this.module == null) {
            this.module = createModule();
        }

        return module;
    }

    @Override
    public <X extends XmlObject> void injectChildMembers(X injectee) {
        getInjector().injectMembers(injectee);
    }

    @Override
    public final <X extends XmlObject> X createTypedObject(Class<X> type) {
        X result = getInjector().getInstance(type);
        injectChildMembers(result);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Object createObject(Class type) {
        return createTypedObject(type);
    }

    @Override
    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass,
        MuseumXmlPostProcessor<? extends XmlObject> p) {
        getInjector().registerXmlPostProcessor(targetClass, p);
    }

    @Override
    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass) {
        getInjector().unregisterXmlPostProcessor(targetClass);
    }
}

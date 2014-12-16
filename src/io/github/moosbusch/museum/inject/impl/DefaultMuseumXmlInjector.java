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
package io.github.moosbusch.museum.inject.impl;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import io.github.moosbusch.museum.inject.MuseumXmlInjector;
import io.github.moosbusch.museum.inject.MuseumXmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public class DefaultMuseumXmlInjector implements MuseumXmlInjector {

    private final Injector wrappedInjector;
    private final Set<MuseumXmlPostProcessor> xmlPostProcessors;

    public DefaultMuseumXmlInjector(Injector wrappedInjector) {
        this.wrappedInjector = wrappedInjector;
        this.xmlPostProcessors = new LinkedHashSet<MuseumXmlPostProcessor>();
        init();
    }

    private void init() {
        xmlPostProcessors.add(new DefaultMuseumXmlPostProcessor());
    }

    protected void notifyPostProcessors(XmlObject injectee) {
        for (MuseumXmlPostProcessor xmlPostProcessor : xmlPostProcessors) {
            xmlPostProcessor.postProcess(this, injectee);
        }
    }

    @Override
    public final void injectMembers(Object injectee) {
        if (injectee != null) {
            if (injectee instanceof XmlObject) {
                notifyPostProcessors((XmlObject) injectee);
            }
        }
    }

    @Override
    public final <T> MembersInjector<T> getMembersInjector(TypeLiteral< T> typeLiteral) {
        return wrappedInjector.getMembersInjector(typeLiteral);
    }

    @Override
    public final <T> MembersInjector<T> getMembersInjector(Class< T> type) {
        return wrappedInjector.getMembersInjector(type);
    }

    @Override
    public final Map<Key<?>, Binding<?>> getBindings() {
        return wrappedInjector.getBindings();
    }

    @Override
    public final Map<Key<?>, Binding<?>> getAllBindings() {
        return wrappedInjector.getAllBindings();
    }

    @Override
    public final <T> Binding<T> getBinding(Key< T> key) {
        return wrappedInjector.getBinding(key);
    }

    @Override
    public final <T> Binding<T> getBinding(Class< T> type) {
        return wrappedInjector.getBinding(type);
    }

    @Override
    public final <T> Binding<T> getExistingBinding(Key< T> key) {
        return wrappedInjector.getExistingBinding(key);
    }

    @Override
    public final <T> List<Binding<T>> findBindingsByType(TypeLiteral< T> type) {
        return wrappedInjector.findBindingsByType(type);
    }

    @Override
    public final <T> Provider<T> getProvider(Key< T> key) {
        return wrappedInjector.getProvider(key);
    }

    @Override
    public final <T> Provider<T> getProvider(Class< T> type) {
        return wrappedInjector.getProvider(type);
    }

    @Override
    public final <T> T getInstance(Key< T> key) {
        return wrappedInjector.getInstance(key);
    }

    @Override
    public final <T> T getInstance(Class< T> type) {
        return wrappedInjector.getInstance(type);
    }

    @Override
    public final Injector getParent() {
        return wrappedInjector.getParent();
    }

    @Override
    public final Injector createChildInjector(Iterable<? extends Module> modules) {
        return wrappedInjector.createChildInjector(modules);
    }

    @Override
    public final Injector createChildInjector(Module... modules) {
        return wrappedInjector.createChildInjector(modules);
    }

    @Override
    public final Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        return wrappedInjector.getScopeBindings();
    }

    @Override
    public final Set<TypeConverterBinding> getTypeConverterBindings() {
        return wrappedInjector.getTypeConverterBindings();
    }

    @Override
    public void registerXmlPostProcessor(MuseumXmlPostProcessor p) {
        xmlPostProcessors.add(p);
    }

    @Override
    public void unregisterXmlPostProcessor(MuseumXmlPostProcessor p) {
        xmlPostProcessors.remove(p);
    }

}

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
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlInjector;
import org.moosbusch.museum.inject.MuseumXmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractMuseumXmlInjector implements MuseumXmlInjector {

    private final Injector wrappedInjector;
    private final Map<Class<? extends XmlObject>, MuseumXmlPostProcessor<? extends XmlObject>> xmlPostProcessors;

    public AbstractMuseumXmlInjector(Injector wrappedInjector) {
        this.wrappedInjector = wrappedInjector;
        this.xmlPostProcessors = new HashMap<Class<? extends XmlObject>, MuseumXmlPostProcessor<? extends XmlObject>>();
    }

    protected Map<Class<? extends XmlObject>, MuseumXmlPostProcessor<? extends XmlObject>> getXmlPostProcessors() {
        return Collections.unmodifiableMap(xmlPostProcessors);
    }

    protected MuseumXmlPostProcessor<? extends XmlObject> getPostProcessorForClass(
            Class<? extends XmlObject> elementClass) {
        if (!getXmlPostProcessors().isEmpty()) {
            MuseumXmlPostProcessor<? extends XmlObject> result
                    = getXmlPostProcessors().get(elementClass);

            if (result != null) {
                return result;
            } else {
                Class<?> superClass = elementClass.getSuperclass();

                if (XmlObject.class.isAssignableFrom(superClass)) {
                    result = getXmlPostProcessors().get(superClass);

                    if (result != null) {
                        return result;
                    } else {
                        Class<?>[] interfaces = elementClass.getInterfaces();

                        for (Class<?> iFace : interfaces) {
                            if (XmlObject.class.isAssignableFrom(iFace)) {
                                result = getXmlPostProcessors().get(iFace);

                                if (result != null) {
                                    return result;
                                }
                            }
                        }
                    }
                }

            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected void notifyPostProcessors(XmlObject injectee) {
        MuseumXmlPostProcessor xmlPostProcessor
                = getPostProcessorForClass(injectee.getClass());

        if (xmlPostProcessor != null) {
            xmlPostProcessor.postProcess(injectee);
        }
    }

    @Override
    public final void injectMembers(Object injectee) {
        if (injectee != null) {
            if (injectee instanceof XmlObject) {
                try {
                    injectXmlMembers((XmlObject) injectee);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(AbstractMuseumXmlInjector.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(AbstractMuseumXmlInjector.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(AbstractMuseumXmlInjector.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    notifyPostProcessors((XmlObject) injectee);
                }
            }
        }
    }

    @Override
    public final <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return wrappedInjector.getMembersInjector(typeLiteral);
    }

    @Override
    public final <T> MembersInjector<T> getMembersInjector(Class<T> type) {
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
    public final <T> Binding<T> getBinding(Key<T> key) {
        return wrappedInjector.getBinding(key);
    }

    @Override
    public final <T> Binding<T> getBinding(Class<T> type) {
        return wrappedInjector.getBinding(type);
    }

    @Override
    public final <T> Binding<T> getExistingBinding(Key<T> key) {
        return wrappedInjector.getExistingBinding(key);
    }

    @Override
    public final <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
        return wrappedInjector.findBindingsByType(type);
    }

    @Override
    public final <T> Provider<T> getProvider(Key<T> key) {
        return wrappedInjector.getProvider(key);
    }

    @Override
    public final <T> Provider<T> getProvider(Class<T> type) {
        return wrappedInjector.getProvider(type);
    }

    @Override
    public final <T> T getInstance(Key<T> key) {
        return wrappedInjector.getInstance(key);
    }

    @Override
    public final <T> T getInstance(Class<T> type) {
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
    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass,
            MuseumXmlPostProcessor<? extends XmlObject> p) {
        xmlPostProcessors.put(targetClass, p);
    }

    @Override
    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass) {
        xmlPostProcessors.remove(targetClass);
    }
}

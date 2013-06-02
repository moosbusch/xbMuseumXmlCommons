/*
 * 
 *
 */
package org.moosbusch.museum.inject.spi;

import com.google.inject.Binding;
import com.google.inject.BindingAnnotation;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.XmlInjector;
import org.moosbusch.museum.inject.evt.XmlInjectionListener;
import org.moosbusch.museum.inject.evt.XmlPostProcessor;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractXmlInjector implements XmlInjector {

    private final Injector wrappedInjector;
    private final Set<XmlInjectionListener> injectionListeners;
    private final Set<XmlPostProcessor> xmlPostProcessors;

    public AbstractXmlInjector(Injector wrappedInjector) {
        this.wrappedInjector = wrappedInjector;
        this.injectionListeners = new HashSet<XmlInjectionListener>();
        this.xmlPostProcessors = new HashSet<XmlPostProcessor>();
    }

    protected abstract void injectXmlMembers(XmlObject instance);

    protected boolean isInjectableSetterMethod(Method m) {
        if ((m.getAnnotation(com.google.inject.Inject.class) != null)
                || (m.getAnnotation(javax.inject.Inject.class) != null)) {
            if ((isPublicInstanceMethod(m)) && (isSetterMethod(m))) {

                if (!isArraySetterMethod(m)) {
                    Class<?>[] paramTypes = m.getParameterTypes();

                    if (paramTypes.length == 1) {
                        Class<?> paramType = paramTypes[0];
                        return (getBinding(paramType) != null);
                    }
                }
            }
        }

        return false;
    }

    protected boolean isPublicInstanceMethod(Method m) {
        return ((m.getModifiers() == Modifier.PUBLIC)
                && (m.getModifiers() != Modifier.STATIC));
    }

    protected boolean isSetterMethod(Method m) {
        return ((m.getName().startsWith(SET_METHOD_PREFIX))
                && (void.class.isAssignableFrom(m.getReturnType())));
    }

    protected boolean isArraySetterMethod(Method m) {
        if (m.getName().endsWith(ARRAY_METHOD_SUFFIX)) {
            Class<?>[] paramTypes = m.getParameterTypes();

            if (paramTypes.length == 1) {
                Class<?> paramType = paramTypes[0];
                return paramType.isArray();
            } else if (paramTypes.length == 2) {
                Class<?> firstParamType = paramTypes[0];
                Class<?> secondParamType = paramTypes[1];
                return ((int.class.isAssignableFrom(firstParamType))
                        && (!secondParamType.isArray()));
            }
        }

        return false;
    }

    protected Annotation getParameterBindingAnnotation(Method m) {
        Annotation[][] paramAnnotations = m.getParameterAnnotations();

        for (Annotation[] annotations : paramAnnotations) {
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType =
                        annotation.annotationType();

                if (annotationType.isAnnotationPresent(BindingAnnotation.class)) {
                    return annotation;
                }
            }
        }

        return null;
    }

    protected Collection<XmlInjectionListener> getInjectionListeners() {
        return Collections.unmodifiableCollection(injectionListeners);
    }

    public Set<XmlPostProcessor> getXmlPostProcessors() {
        return Collections.unmodifiableSet(xmlPostProcessors);
    }

    protected Object createParameterValue(Method m, Class<?> paramType,
            Annotation bindingAnnotation) {
        Key<?> bindingKey;

        if (bindingAnnotation != null) {
            bindingKey = Key.get(paramType, bindingAnnotation);
        } else {
            bindingKey = Key.get(paramType);
        }

        return wrappedInjector.getInstance(bindingKey);
    }

    protected void fireBeforeInjectionEvent(XmlObject injectee) {
        for (XmlInjectionListener listener : getInjectionListeners()) {
            listener.beforeChildMembersInjection(injectee);
        }
    }

    protected void fireAfterInjectionEvent(XmlObject injectee) {
        for (XmlInjectionListener listener : getInjectionListeners()) {
            listener.afterChildMembersInjection(injectee);
        }
    }

    protected void notifyPostProcessors(XmlObject instance) {
        for (XmlPostProcessor xmlPostProcessor
                : getXmlPostProcessors()) {
            Class<? extends XmlObject> targetClass =
                    xmlPostProcessor.getTargetClass();

            if (targetClass.isAssignableFrom(instance.getClass())) {
                xmlPostProcessor.postProcess(instance);
            }
        }
    }

    @Override
    public final void injectMembers(Object instance) {
        if (instance != null) {
            if (instance instanceof XmlObject) {
                fireBeforeInjectionEvent((XmlObject) instance);
                injectXmlMembers((XmlObject) instance);
                fireAfterInjectionEvent((XmlObject) instance);
                notifyPostProcessors((XmlObject) instance);
            }
        }
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return wrappedInjector.getMembersInjector(typeLiteral);
    }

    @Override
    public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
        return wrappedInjector.getMembersInjector(type);
    }

    @Override
    public Map<Key<?>, Binding<?>> getBindings() {
        return wrappedInjector.getBindings();
    }

    @Override
    public Map<Key<?>, Binding<?>> getAllBindings() {
        return wrappedInjector.getAllBindings();
    }

    @Override
    public <T> Binding<T> getBinding(Key<T> key) {
        return wrappedInjector.getBinding(key);
    }

    @Override
    public <T> Binding<T> getBinding(Class<T> type) {
        return wrappedInjector.getBinding(type);
    }

    @Override
    public <T> Binding<T> getExistingBinding(Key<T> key) {
        return wrappedInjector.getExistingBinding(key);
    }

    @Override
    public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
        return wrappedInjector.findBindingsByType(type);
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        return wrappedInjector.getProvider(key);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return wrappedInjector.getProvider(type);
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return wrappedInjector.getInstance(key);
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return wrappedInjector.getInstance(type);
    }

    @Override
    public Injector getParent() {
        return wrappedInjector.getParent();
    }

    @Override
    public Injector createChildInjector(Iterable<? extends Module> modules) {
        return wrappedInjector.createChildInjector(modules);
    }

    @Override
    public Injector createChildInjector(Module... modules) {
        return wrappedInjector.createChildInjector(modules);
    }

    @Override
    public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        return wrappedInjector.getScopeBindings();
    }

    @Override
    public Set<TypeConverterBinding> getTypeConverterBindings() {
        return wrappedInjector.getTypeConverterBindings();
    }

    @Override
    public void addInjectionListener(XmlInjectionListener e) {
        injectionListeners.add(e);
    }

    @Override
    public void removeInjectionListener(XmlInjectionListener o) {
        injectionListeners.remove(o);
    }

    @Override
    public void addXmlPostProcessor(XmlPostProcessor p) {
        xmlPostProcessors.add(p);
    }

    @Override
    public void removeXmlPostProcessor(XmlPostProcessor p) {
        xmlPostProcessors.remove(p);
    }
}

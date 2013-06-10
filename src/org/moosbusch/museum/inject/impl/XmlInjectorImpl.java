/*
 *
 *
 */
package org.moosbusch.museum.inject.impl;

import com.google.inject.Injector;
import com.google.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.XmlInjector;
import org.moosbusch.museum.inject.spi.AbstractXmlInjector;
import org.moosbusch.museum.inject.util.XmlInjectionUtil;

/**
 *
 * @author moosbusch
 */
public class XmlInjectorImpl extends AbstractXmlInjector {

    public XmlInjectorImpl(Injector wrappedInjector) {
        super(wrappedInjector);
    }

    protected boolean isInjectionNeeded(XmlObject instance, Method method) {
        SchemaType schemaType = instance.schemaType();
        SchemaProperty[] properties = schemaType.getProperties();
        String methodName = method.getName();
        String fieldName;

        if ((methodName.endsWith(XmlInjector.ARRAY_METHOD_SUFFIX))
                || (methodName.endsWith(XmlInjector.ARRAY_METHOD_SUFFIX))) {
            return false;
        }

        if (methodName.startsWith(XmlInjector.SET_METHOD_PREFIX)) {
            fieldName = methodName.substring(3);

            for (SchemaProperty property : properties) {
                if (property.getJavaPropertyName().equals(fieldName)) {
                    return (property.getMinOccurs().intValue() > 0);
                }
            }
        }

        return true;
    }

    @Override
    public void injectXmlMembers(XmlObject injectee) {
        Set<InjectionPoint> injectionPoints =
                InjectionPoint.forInstanceMethodsAndFields(
                injectee.getClass());

        for (InjectionPoint injectionPoint : injectionPoints) {
            Member member = injectionPoint.getMember();

            if (member instanceof Method) {
                Method method = (Method) member;

                if (XmlInjectionUtil.isInjectableSetterMethod(this, method)) {
                    Annotation bindingAnnotation =
                            XmlInjectionUtil.getParameterBindingAnnotation(method);
                    Class<?> parameterType = method.getParameterTypes()[0];
                    Object paramValue = XmlInjectionUtil.createParameterValue(
                            this, parameterType, bindingAnnotation);

                    if (isInjectionNeeded(injectee, method)) {
                        try {
                            method.invoke(injectee, paramValue);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(XmlInjectorImpl.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(XmlInjectorImpl.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(XmlInjectorImpl.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }
}

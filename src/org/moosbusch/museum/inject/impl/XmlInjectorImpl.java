/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.spi.AbstractXmlInjector;

/**
 *
 * @author moosbusch
 */
public class XmlInjectorImpl extends AbstractXmlInjector {

    public XmlInjectorImpl(Injector wrappedInjector) {
        super(wrappedInjector);
    }

    @Override
    protected void injectXmlMembers(XmlObject instance) {
        Set<InjectionPoint> injectionPoints =
                InjectionPoint.forInstanceMethodsAndFields(
                instance.getClass());

        for (InjectionPoint injectionPoint : injectionPoints) {
            Member member = injectionPoint.getMember();

            if (member instanceof Method) {
                Method method = (Method) member;

                if (isInjectableSetterMethod(method)) {
                    Annotation bindingAnnotation =
                            getParameterBindingAnnotation(method);
                    Class<?> parameterType = method.getParameterTypes()[0];
                    Object paramValue = createParameterValue(
                            method, parameterType, bindingAnnotation);

                    //                    SchemaType schemaType = instance.schemaType();
                    //                    SchemaProperty[] attributeProperties =
                    //                            schemaType.getAttributeProperties();
                    //                    SchemaProperty[] elementProperties =
                    //                            schemaType.getElementProperties();

                    try {
                        method.invoke(instance, paramValue);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(XmlInjectorImpl.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(XmlInjectorImpl.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(XmlInjectorImpl.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } finally {

                    }
                }
            }
        }
    }





}

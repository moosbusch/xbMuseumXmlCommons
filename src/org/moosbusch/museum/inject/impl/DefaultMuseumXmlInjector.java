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
package org.moosbusch.museum.inject.impl;

import com.google.inject.Injector;
import com.google.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Set;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.inject.MuseumXmlInjector;
import org.moosbusch.museum.inject.spi.AbstractMuseumXmlInjector;
import org.moosbusch.museum.inject.util.XmlInjectionUtil;

/**
 *
 * @author moosbusch
 */
public class DefaultMuseumXmlInjector extends AbstractMuseumXmlInjector {

    public DefaultMuseumXmlInjector(Injector wrappedInjector) {
        super(wrappedInjector);
    }

    protected boolean isInjectionNeeded(XmlObject instance, Method method) {
        SchemaType schemaType = instance.schemaType();
        return (! XmlInjectionUtil.getRequiredProperties(schemaType).isEmpty());
    }

    @Override
    public void injectXmlMembers(final XmlObject injectee) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
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
                        method.invoke(injectee, paramValue);
                    }
                }
            }
        }
    }
}

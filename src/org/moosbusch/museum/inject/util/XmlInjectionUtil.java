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
package org.moosbusch.museum.inject.util;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.moosbusch.museum.inject.XmlInjector;
import static org.moosbusch.museum.inject.XmlInjector.ARRAY_METHOD_SUFFIX;
import static org.moosbusch.museum.inject.XmlInjector.SET_METHOD_PREFIX;

/**
 *
 * @author moosbusch
 */
public class XmlInjectionUtil {

    public static boolean isInjectableSetterMethod(XmlInjector injector, Method m) {
        if ((m.getAnnotation(com.google.inject.Inject.class) != null)
                || (m.getAnnotation(javax.inject.Inject.class) != null)) {
            if ((isPublicInstanceMethod(m)) && (isSetterMethod(m))) {

                if (!isArraySetterMethod(m)) {
                    Class<?>[] paramTypes = m.getParameterTypes();

                    if (paramTypes.length == 1) {
                        Class<?> paramType = paramTypes[0];
                        return (injector.getBinding(paramType) != null);
                    }
                }
            }
        }

        return false;
    }

    public static boolean isPublicInstanceMethod(Method m) {
        return ((m.getModifiers() == Modifier.PUBLIC)
                && (m.getModifiers() != Modifier.STATIC));
    }

    public static boolean isSetterMethod(Method m) {
        return ((m.getName().startsWith(SET_METHOD_PREFIX))
                && (void.class.isAssignableFrom(m.getReturnType())));
    }

    public static boolean isArraySetterMethod(Method m) {
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

    public static Annotation getParameterBindingAnnotation(Method m) {
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

    public static Object createParameterValue(XmlInjector injector, Class<?> paramType,
            Annotation bindingAnnotation) {
        Key<?> bindingKey;
        Object result;

        if (bindingAnnotation != null) {
            bindingKey = Key.get(paramType, bindingAnnotation);
        } else {
            bindingKey = Key.get(paramType);
        }

        result = injector.getInstance(bindingKey);
        injector.injectMembers(result);
        return result;
    }
}

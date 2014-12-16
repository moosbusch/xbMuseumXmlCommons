/*
 * Copyright 2014 Gunnar Kappei.
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

import com.google.inject.Key;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import io.github.moosbusch.museum.document.MuseumXmlDocument;
import io.github.moosbusch.museum.inject.MuseumXmlInjector;
import static io.github.moosbusch.museum.inject.MuseumXmlInjector.ADD_METHOD_NAME;
import static io.github.moosbusch.museum.inject.MuseumXmlInjector.GET_METHOD_PREFIX;
import static io.github.moosbusch.museum.inject.MuseumXmlInjector.LIST_METHOD_SUFFIX;
import static io.github.moosbusch.museum.inject.MuseumXmlInjector.SET_METHOD_PREFIX;
import io.github.moosbusch.museum.inject.MuseumXmlPostProcessor;
import io.github.moosbusch.museum.inject.util.XmlPostProcessorInfo;

/**
 *
 * @author Gunnar Kappei
 */
public final class DefaultMuseumXmlPostProcessor implements MuseumXmlPostProcessor {

    private final Map<String, String> stringMappingMap;

    public DefaultMuseumXmlPostProcessor() {
        this.stringMappingMap = Objects.requireNonNull(initStringMappings());
    }

    private Map<String, String> initStringMappings() {
        return createStringMappings();
    }

    protected Map<String, String> createStringMappings() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(LANGUAGE_SETTER_METHOD_NAME,
                MuseumXmlDocument.DEFAULT_LANGUAGE_VALUE);
        result.put(LANGUAGE_ENCODING_SETTER_METHOD_NAME,
                MuseumXmlDocument.DEFAULT_LANGUAGE_ENCODING_VALUE);
        result.put(RELATED_ENCODING_SETTER_METHOD_NAME,
                MuseumXmlDocument.DEFAULT_RELATED_ENCODING_VALUE);
        result.put(ENCODING_ANALOG_SETTER_METHOD_NAME,
                MuseumXmlDocument.DEFAULT_ENCODING_ANALOG_VALUE);
        return result;
    }

    protected Object createParameterValue(MuseumXmlInjector injector, Class<?> paramType) {
        Key<?> bindingKey;
        Object result = null;

        bindingKey = Key.get(paramType);

        try {
            result = Objects.requireNonNull(injector.getInstance(bindingKey));
        } finally {
            injector.injectMembers(result);
        }

        return result;
    }

    protected String createInsertionMethodName(SchemaProperty schemaProperty) {
        String propertyName = schemaProperty.getJavaPropertyName();

        if (schemaProperty.extendsJavaArray()) {
            return GET_METHOD_PREFIX + propertyName + LIST_METHOD_SUFFIX;
        }

        return SET_METHOD_PREFIX + propertyName;
    }

    protected XmlPostProcessorInfo createPostProcessorInfo(XmlObject target,
            String insertionMethodName) {
        Method[] methods = target.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().equals(insertionMethodName)) {
                Class<?>[] paramTypes = method.getParameterTypes();

                if (paramTypes.length == 1) {
                    return new XmlPostProcessorInfo(target, method);
                } else if ((paramTypes.length == 0)
                        && (insertionMethodName.startsWith(GET_METHOD_PREFIX))) {
                    Method listGetter;

                    try {
                        listGetter = target.getClass().getMethod(insertionMethodName);
                        List<?> elementsList = (List) listGetter.invoke(target);

                        for (Method m : elementsList.getClass().getMethods()) {
                            if (m.getName().equals(ADD_METHOD_NAME)) {
                                if (!m.isAccessible()) {
                                    m.setAccessible(true);
                                }

                                return new XmlPostProcessorInfo(elementsList, m);
                            }
                        }
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                                Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                }
            }
        }

        throw new IllegalStateException(
                "Cannot find matching setter-method named '" + insertionMethodName
                + "' to be used for dependency-injection on class '"
                + target.getClass().getCanonicalName() + "'.");
    }

    protected synchronized void insertDefaultValue(
            XmlPostProcessorInfo postProcessorInfo, Object parameterValue) {
        try {
            if (!postProcessorInfo.isListInsertion()) {
                postProcessorInfo.getInsertionMethod().invoke(
                        postProcessorInfo.getTarget(), parameterValue);
            } else {
                postProcessorInfo.getInsertionMethod().invoke(
                        postProcessorInfo.getTarget(), 0, parameterValue);
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(DefaultMuseumXmlInjector.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    @Override
    public void postProcess(MuseumXmlInjector injector, XmlObject injectee) {
        SchemaType schemaType = injectee.schemaType();
        SchemaProperty[] schemaProperties = schemaType.getProperties();

        for (SchemaProperty schemaProperty : schemaProperties) {
            if (schemaProperty.getMinOccurs().longValue() > 0) {
                String insertionMethodName = createInsertionMethodName(schemaProperty);
                XmlPostProcessorInfo postProcessorInfo = createPostProcessorInfo(
                        injectee, insertionMethodName);
                Class<?> paramType = postProcessorInfo.getInsertionParameterType();
                Object paramValue;

                if (paramType != String.class) {
                    paramValue = createParameterValue(injector, paramType);
                } else {
                    paramValue = getStringMappings().get(insertionMethodName);

                    if (paramValue == null) {
                        paramValue = MuseumXmlDocument.GENERIC_STRING_VALUE;
                    } else if (paramValue.equals("")) {
                        paramValue = MuseumXmlDocument.GENERIC_STRING_VALUE;
                    }
                }

                insertDefaultValue(postProcessorInfo, paramValue);
            }
        }
    }

    @Override
    public final Map<String, String> getStringMappings() {
        return Collections.unmodifiableMap(stringMappingMap);
    }

}

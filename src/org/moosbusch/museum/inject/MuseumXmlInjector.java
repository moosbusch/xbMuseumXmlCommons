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
package org.moosbusch.museum.inject;

import com.google.inject.Injector;
import java.lang.reflect.InvocationTargetException;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlInjector extends Injector {

    public static final String SET_METHOD_PREFIX = "set";
    public static final String ARRAY_METHOD_SUFFIX = "Array";

    @Override
    public void injectMembers(Object injectee);

    public void injectXmlMembers(XmlObject injectee) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException;

    public void registerXmlPostProcessor(Class<? extends XmlObject> targetClass,
            MuseumXmlPostProcessor<? extends XmlObject> p);

    public void unregisterXmlPostProcessor(Class<? extends XmlObject> targetClass);

}

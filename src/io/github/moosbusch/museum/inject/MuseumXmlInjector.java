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
package io.github.moosbusch.museum.inject;

import com.google.inject.Injector;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlInjector extends Injector {

    public static final String ADD_METHOD_NAME = "add";
    public static final String GET_METHOD_PREFIX = "get";
    public static final String SET_METHOD_PREFIX = "set";
    public static final String LIST_METHOD_SUFFIX = "List";

    @Override
    public void injectMembers(Object injectee);

//    public void injectXmlMembers(XmlObject injectee) throws IllegalAccessException,
//            IllegalArgumentException, InvocationTargetException;

    public void registerXmlPostProcessor(MuseumXmlPostProcessor p);

    public void unregisterXmlPostProcessor(MuseumXmlPostProcessor p);

}

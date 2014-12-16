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

import java.util.Map;
import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlPostProcessor {

    public static final String LANGUAGE_SETTER_METHOD_NAME = "setLang";
    public static final String LANGUAGE_ENCODING_SETTER_METHOD_NAME
            = "setLanguageencoding";
    public static final String RELATED_ENCODING_SETTER_METHOD_NAME
            = "setRelatedencoding";
    public static final String ENCODING_ANALOG_SETTER_METHOD_NAME
            = "setEncodinganalog";

    public void postProcess(MuseumXmlInjector injector, XmlObject xmlObject);

    public Map<String, String> getStringMappings();
}

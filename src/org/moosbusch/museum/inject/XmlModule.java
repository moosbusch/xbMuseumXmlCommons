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

import com.google.inject.Module;
import com.google.inject.Provides;
import org.apache.xmlbeans.XmlObject;
import org.moosbusch.museum.document.XmlDocument;

/**
 *
 * @author moosbusch
 */
public interface XmlModule extends Module {

    @Provides
    public XmlDocument<? extends XmlObject, ? extends XmlObjectFactory
            <? extends XmlModule, ? extends XmlObject>,
            ? extends XmlObject, ? extends XmlObject> createDocument();

    public String getLanguage();

}

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
package org.moosbusch.museum.inject.spi;

import com.google.inject.AbstractModule;
import org.moosbusch.museum.inject.XmlModule;

/**
 *
 * @author moosbusch
 */
public abstract class AbstractXmlModule extends AbstractModule
        implements XmlModule {

    private String language;

    public AbstractXmlModule() {
        this.language = initLanguage();
    }

    protected abstract String createLanguage();

    protected abstract void configureImpl();

    @Override
    protected final void configure() {
        binder().disableCircularProxies();
        configureImpl();
    }

    private String initLanguage() {
        return createLanguage();
    }

    @Override
    public String getLanguage() {
        return language;
    }

}

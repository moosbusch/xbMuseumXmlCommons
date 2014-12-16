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

package io.github.moosbusch.museum.inject.util;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author Gunnar Kappei
 */
public final class XmlPostProcessorInfo {

    private final Method insertionMethod;
    private final Object target;

    public XmlPostProcessorInfo(Object target, Method insertionMethod) {
        this.insertionMethod = insertionMethod;
        this.target = target;
    }

    public Method getInsertionMethod() {
        return insertionMethod;
    }

    public Object getTarget() {
        return target;
    }

    public boolean isListInsertion() {
        return (getTarget() instanceof List);
    }

    public Class<?> getInsertionParameterType() {
        if (isListInsertion()) {
            return getInsertionMethod().getParameterTypes()[1];
        }

        return getInsertionMethod().getParameterTypes()[0];
    }

}

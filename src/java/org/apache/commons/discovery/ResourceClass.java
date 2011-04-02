/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.discovery;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.commons.discovery.log.DiscoveryLogFactory;
import org.apache.commons.logging.Log;


/**
 * 'Resource' located by discovery.
 * Naming of methods becomes a real pain ('getClass()')
 * so I've patterned this after ClassLoader...
 * 
 * I think it works well as it will give users a point-of-reference.
 * 
 * @author Richard A. Sitze
 */
public class ResourceClass<T> extends Resource
{
    private static Log log = DiscoveryLogFactory.newLog(ResourceClass.class);
    public static void setLog(Log _log) {
        log = _log;
    }
    protected Class<? extends T>       resourceClass;

    public <S extends T> ResourceClass(Class<S> resourceClass, URL resource) {
        super(resourceClass.getName(), resource, resourceClass.getClassLoader());
        this.resourceClass = resourceClass;
    }

    public ResourceClass(String resourceName, URL resource, ClassLoader loader) {
        super(resourceName, resource, loader);
    }
    
    /**
     * Get the value of resourceClass.
     * Loading the class does NOT guarentee that the class can be
     * instantiated.  Go figure.
     * The class can be instantiated when the class is linked/resolved,
     * and all dependencies are resolved.
     * Various JDKs do this at different times, so beware:
     * java.lang.NoClassDefFoundError when
     * calling Class.getDeclaredMethod() (JDK14),
     * java.lang.reflect.InvocationTargetException
     * (wrapping java.lang.NoClassDefFoundError) when calling
     * java.lang.newInstance (JDK13),
     * and who knows what else..
     *
     * @return value of resourceClass.
     */
    public <S extends T> Class<S> loadClass() {
        if (resourceClass == null  &&  getClassLoader() != null) {
            if (log.isDebugEnabled())
                log.debug("loadClass: Loading class '" + getName() + "' with " + getClassLoader());

            resourceClass = AccessController.doPrivileged(
                new PrivilegedAction<Class<? extends T>>() {
                    public Class<? extends T> run() {
                        try {
                            @SuppressWarnings("unchecked") // this can raise a ClassCastException at runtime
                            Class<S> returned = (Class<S>)  (Class<S>) getClassLoader().loadClass(getName());
                            return returned;
                        } catch (ClassNotFoundException e) {
                            return null;
                        }
                    }
                });
        }

        @SuppressWarnings("unchecked") // this is assumed by default, see the ctor
        Class<S> returned = (Class<S>) resourceClass;
        return returned;
    }
    
    public String toString() {
        return "ResourceClass[" + getName() +  ", " + getResource() + ", " + getClassLoader() + "]";
    }
}

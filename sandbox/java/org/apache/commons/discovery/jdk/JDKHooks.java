/*
 * Copyright 1999-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.discovery.jdk;

import java.util.Enumeration;
import java.io.IOException;


/**
 * @author Richard A. Sitze
 */
public abstract class JDKHooks {
    private static final JDKHooks jdkHooks;
    
    static {
        jdkHooks = new JDK12Hooks();
    }
    
    protected JDKHooks() { }
    
    /**
     * Return singleton object representing JVM hooks/tools.
     * 
     * TODO: add logic to detect JDK level.
     */
    public static final JDKHooks getJDKHooks() {
        return jdkHooks;
    }

    /**
     * The thread context class loader is available for JDK 1.2
     * or later, if certain security conditions are met.
     * 
     * @return The thread context class loader, if available.
     *         Otherwise return null.
     */
    public abstract ClassLoader getThreadContextClassLoader();

    /**
     * The system class loader is available for JDK 1.2
     * or later, if certain security conditions are met.
     * 
     * @return The system class loader, if available.
     *         Otherwise return null.
     */
    public abstract ClassLoader getSystemClassLoader();
    
    public abstract Enumeration getResources(ClassLoader loader,
                                             String resourceName)
        throws IOException;
}
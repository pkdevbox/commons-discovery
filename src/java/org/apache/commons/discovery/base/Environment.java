/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.discovery.base;

import org.apache.commons.discover.jdk.JDKHooks;
import org.apache.commons.discovery.DiscoverClass;
import org.apache.commons.discovery.tools.ClassLoaderUtils;


/**
 * Represents a environment context:
 * the Thread Context Class Loader, the group context id, and the
 * root discovery class (class representing the discovery front-door).
 * 
 * @author Richard A. Sitze
 */
public class Environment {
    /**
     * Readable placeholder for a null value.
     */
    public static final String     defaultGroupContext = null;
    
    public static final Class      defaultRootDiscoveryClass
        = DiscoverClass.class;

    /**
     * Thread context class loader or null if not available (JDK 1.1).
     * Wrapped bootstrap classloader if classLoader == null.
     */
    private final ClassLoader threadContextClassLoader =
        JDKHooks.getJDKHooks().getThreadContextClassLoader();

    private final String groupContext;

    private final Class rootDiscoveryClass;
    private final Class callingClass;
    
    private boolean searchLibOnly;
    
    public Environment() {
        this(defaultGroupContext, defaultRootDiscoveryClass, null);
    }
    
    public Environment(Class rootDiscoveryClass) {
        this(defaultGroupContext, rootDiscoveryClass, null);
    }
    
    public Environment(String groupContext) {
        this(groupContext, defaultRootDiscoveryClass, null);
    }
    
    public Environment(String groupContext, Class rootDiscoveryClass) {
        this(groupContext, rootDiscoveryClass, null);
    }
    
    public Environment(String groupContext,
                       Class rootDiscoveryClass,
                       Class callingClass) {
        this.groupContext = groupContext;

        this.rootDiscoveryClass =
            (rootDiscoveryClass == null)
            ? defaultRootDiscoveryClass
            : rootDiscoveryClass;

        /**
         * Default to rootDiscoveryClass only if
         * rootDiscoveryClass != defaultRootDiscoveryClass
         * (otherwise it changes classloader order, see load.Loaders).
         * MAY BE NULL.
         */
        this.callingClass =
            (callingClass == null  &&
             rootDiscoveryClass != defaultRootDiscoveryClass)
            ? rootDiscoveryClass
            : callingClass;
            
        this.searchLibOnly = false;
    }
    
    public ClassLoader getThreadContextClassLoader() {
        return threadContextClassLoader;
    }
    
    public String getGroupContext() {
        return groupContext;
    }
    
    public Class getRootDiscoveryClass() {
        return rootDiscoveryClass;
    }

    /**
     * May be null
     */    
    public Class getCallingClass() {
        return callingClass;
    }
    
    public void setSearchLibOnly(boolean searchLibOnly) {
        this.searchLibOnly = searchLibOnly;
    }
    
    public boolean getSearchLibOnly() {
        return searchLibOnly;
    }
}
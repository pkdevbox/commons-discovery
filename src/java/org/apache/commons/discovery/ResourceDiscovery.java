/*
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

package org.apache.commons.discovery;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.discover.jdk.JDKHooks;
import org.apache.commons.discovery.base.ClassLoaders;


/**
 * This class supports any VM, including JDK1.1, via
 * org.apache.commons.discover.jdk.JDKHooks.
 *
 * The findResources() method will check every loader.
 *
 * @author Richard A. Sitze
 * @author Craig R. McClanahan
 * @author Costin Manolache
 * @author James Strachan
 */
public class ResourceDiscovery
{
    /**
     * this doesn't buy anything except +/- style (subjective).
     */
    protected static JDKHooks jdkHooks = JDKHooks.getJDKHooks();
    
    private ClassLoaders classLoaders;
    
    /** Construct a new resource discoverer
     */
    public ResourceDiscovery() {
        setClassLoaders(new ClassLoaders());
    }
    
    /** Construct a new resource discoverer
     */
    public ResourceDiscovery(ClassLoaders classLoaders) {
        setClassLoaders(classLoaders);
    }

    /**
     * @deprecated
     */
    public static ResourceDiscovery newInstance() {
        // This is _not_ singleton.
        return new ResourceDiscovery();
        // XXX Check if JDK1.1 is used no longer necessary.
    }

    /**
     * Specify set of class loaders to be used in searching.
     */
    public void setClassLoaders(ClassLoaders loaders) {
        classLoaders = loaders;
    }
    
    protected ClassLoaders getClassLoaders() {
        return classLoaders;
    }

    /**
     * Specify a new class loader to be used in searching.
     * The order of loaders determines the order of the result.
     * It is recommended to add the most specific loaders first.
     */
    public void addClassLoader(ClassLoader loader) {
        classLoaders.put(loader);
    }

    /**
     * This gets ugly, but...
     * a) it preserves the desired behaviour
     * b) it defers file I/O and class loader lookup until necessary.
     * 
     * @return Enumeration of ResourceInfo
     */
    public Enumeration findResources(final String resourceName) {
        return new Enumeration() {
            private int idx = 0;
            private ClassLoader loader = null;
            private Enumeration resources = null;
            private ResourceInfo resource = null;
            
            public boolean hasMoreElements() {
                if (resource == null) {
                    resource = getNextResource();
                }
                return resource != null;
            }
            
            public Object nextElement() {
                Object element = resource;
                resource = null;
                return element;
            }
            
            private ResourceInfo getNextResource() {
                if (resources == null || !resources.hasMoreElements()) {
                    resources = getNextResources();
                }

                ResourceInfo resourceInfo;
                if (resources != null) {
                    URL url = (URL)resources.nextElement();
                    System.out.println("XXX URL " + url );
                    resourceInfo = new ResourceInfo(resourceName, loader, url);
                    System.out.println("XXX " + resourceInfo.toString());
                } else {
                    resourceInfo = null;
                }
                
                return resourceInfo;
            }
            
            private Enumeration getNextResources() {
                while (idx < getClassLoaders().size()) {
                    loader = getClassLoaders().get(idx++);
                    try {
                        Enumeration enum = jdkHooks.getResources(loader, resourceName);
                        if (enum != null && enum.hasMoreElements()) {
                            return enum;
                        }
                    } catch( IOException ex ) {
                        ex.printStackTrace();
                    }
                }
                return null;
            }
        };
    }
}
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

import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.discovery.log.DiscoveryLogFactory;
import org.apache.commons.logging.Log;



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
public class ClassDiscovery extends ResourceDiscovery
{
    private static Log log = DiscoveryLogFactory.newLog(ClassDiscovery.class);
    public static void setLog(Log _log) {
        log = _log;
    }

    protected static final String SERVICE_HOME = "META-INF/services/";
    
    /** Construct a new class discoverer
     */
    public ClassDiscovery() {
    }
    
    /** Construct a new class discoverer
     */
    public ClassDiscovery(ClassLoaders classLoaders) {
        super(classLoaders);
    }
    
    /**
     * This gets ugly, but...
     * a) it preserves the desired behaviour
     * b) it defers file I/O and class loader lookup until necessary.
     * 
     * Find upto one class per class loader, and don't load duplicates
     * from different class loaders (first one wins).
     * 
     * @return Enumeration of ClassInfo
     */
    public Enumeration find(String className) {
        return findClasses(className);
    }
    
    protected Enumeration findClasses(final String className) {
        final String resourceName = className.replace('.','/') + ".class";
        
        if (log.isDebugEnabled())
            log.debug("findClasses: className='" + className + "'");

        return new Enumeration() {
            private Vector history = new Vector();
            private int idx = 0;
            private ClassInfo resource = null;
            
            public boolean hasMoreElements() {
                if (resource == null) {
                    resource = getNextClass();
                }
                return resource != null;
            }
            
            public Object nextElement() {
                Object element = resource;
                resource = null;
                return element;
            }
            
            private ClassInfo getNextClass() {
                while (idx < getClassLoaders().size()) {
                    ClassLoader loader = getClassLoaders().get(idx++);
                    URL url = loader.getResource(resourceName);
                    if (url != null) {
                        if (!history.contains(url)) {
                            history.addElement(url);
    
                            if (log.isDebugEnabled())
                                log.debug("getNextClass: next URL='" + url + "'");
    
                            return new ClassInfo(className, loader, url);
                        }
                        if (log.isDebugEnabled())
                            log.debug("getNextClass: duplicate URL='" + url + "'");
                    } else {
                        if (log.isDebugEnabled())
                            log.debug("getNextClass: loader " + loader + ": '" + resourceName + "' not found");
                    }
                }
                return null;
            }
        };
    }
}

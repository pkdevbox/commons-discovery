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
package org.apache.commons.discovery.tools;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.discovery.ResourceClass;
import org.apache.commons.discovery.listeners.GatherResourceClassesListener;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.classes.DiscoverClasses;
import org.apache.commons.discovery.resource.names.DiscoverServiceNames;


/**
 * [this was ServiceDiscovery12... the 1.1 versus 1.2 issue
 * has been abstracted to org.apache.commons.discover.jdk.JDKHooks]
 * 
 * <p>Implement the JDK1.3 'Service Provider' specification.
 * ( http://java.sun.com/j2se/1.3/docs/guide/jar/jar.html )
 * </p>
 *
 * This class supports any VM, including JDK1.1, via
 * org.apache.commons.discover.jdk.JDKHooks.
 *
 * The caller will first configure the discoverer by adding ( in the desired
 * order ) all the places to look for the META-INF/services. Currently
 * we support loaders.
 *
 * The findResources() method will check every loader.
 *
 * @author Richard A. Sitze
 * @author Craig R. McClanahan
 * @author Costin Manolache
 * @author James Strachan
 */
public class Service
{
    /** Construct a new service discoverer
     */
    protected Service() {
    }
    
    /**
     * as described in
     * sun/jdk1.3.1/docs/guide/jar/jar.html#Service Provider,
     * Except this uses <code>Enumeration</code>
     * instead of <code>Interator</code>.
     * 
     * @return Enumeration of class instances (<code>Object</code>)
     */
    public static Enumeration providers(Class spiClass) {
        return providers(new SPInterface(spiClass), null);
    }
    
    /**
     * This version lets you specify constructor arguments..
     * 
     * @param spi SPI to look for and load.
     * @param classLoaders loaders to use in search.
     *        If <code>null</code> then use ClassLoaders.getAppLoaders().
     */
    public static Enumeration providers(final SPInterface spi,
                                        ClassLoaders loaders)
    {
        if (loaders == null) {
            loaders = ClassLoaders.getAppLoaders(spi.getSPClass(),
                                                 Service.class,
                                                 true);
        }
        
        
        GatherResourceClassesListener listener = new GatherResourceClassesListener();

        DiscoverClasses classDiscovery = new DiscoverClasses(loaders);
        classDiscovery.setListener(listener);

        DiscoverServiceNames discoverServices = new DiscoverServiceNames(loaders);
        discoverServices.setListener(classDiscovery);
        discoverServices.find(spi.getSPName());

        final Vector results = listener.getResourceClasses();
        
        return new Enumeration() {
            private Object obj = null;
            private int idx = 0;
            
            public boolean hasMoreElements() {
                if (obj == null) {
                    obj = getNextElement();
                }
                return obj != null;
            }
            
            public Object nextElement() {
                Object o = obj;
                obj = null;
                return o;
            }
            
            private Object getNextElement() {
                while (idx < results.size()) {
                    try {
                        return spi.newInstance(((ResourceClass)results.get(idx++)).loadClass());
                    } catch (Exception e) {
                        // ignore, retry
                    }
                }
                return null;
            }
        };
    }
}
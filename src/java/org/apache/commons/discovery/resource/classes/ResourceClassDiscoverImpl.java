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

package org.apache.commons.discovery.resource.classes;

import org.apache.commons.discovery.ResourceClass;
import org.apache.commons.discovery.ResourceClassDiscover;
import org.apache.commons.discovery.ResourceClassIterator;
import org.apache.commons.discovery.ResourceIterator;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.ResourceDiscoverImpl;


/**
 * @author Richard A. Sitze
 */
public abstract class ResourceClassDiscoverImpl
    extends ResourceDiscoverImpl
    implements ResourceClassDiscover
{
    /**
     * Construct a new resource discoverer
     */
    public ResourceClassDiscoverImpl() {
        super();
    }
    
    /**
     *  Construct a new resource discoverer
     */
    public ResourceClassDiscoverImpl(ClassLoaders classLoaders) {
        super(classLoaders);
    }


    /**
     * Locate names of resources that are bound to <code>resourceName</code>.
     * 
     * @return ResourceNameIterator
     */
    public ResourceNameIterator findResourceNames(String resourceName) {
        return findResourceClasses(resourceName);
    }

    /**
     * Locate names of resources that are bound to <code>resourceNames</code>.
     * 
     * @return ResourceNameIterator
     */
    public ResourceNameIterator findResourceNames(ResourceNameIterator resourceNames) {
        return findResourceClasses(resourceNames);
    }

    /**
     * Locate resources that are bound to <code>resourceName</code>.
     * 
     * @return ResourceIterator
     */
    public ResourceIterator findResources(String resourceName) {
        return findResourceClasses(resourceName);
    }

    /**
     * Locate resources that are bound to <code>resourceNames</code>.
     * 
     * @return ResourceIterator
     */
    public ResourceIterator findResources(ResourceNameIterator resourceNames) {
        return findResourceClasses(resourceNames);
    }


    /**
     * Locate class resources that are bound to <code>className</code>.
     * 
     * @return ResourceClassIterator
     */
    public abstract ResourceClassIterator findResourceClasses(String className);

    /**
     * Locate class resources that are bound to <code>resourceNames</code>.
     * 
     * @return ResourceIterator
     */
    public ResourceClassIterator findResourceClasses(final ResourceNameIterator inputNames) {
        return new ResourceClassIterator() {
            private ResourceClassIterator classes = null;
            private ResourceClass resource = null;
            
            public boolean hasNext() {
                if (resource == null) {
                    resource = getNextResource();
                }
                return resource != null;
            }
            
            public ResourceClass nextResourceClass() {
                ResourceClass rsrc = resource;
                resource = null;
                return rsrc;
            }
            
            private ResourceClass getNextResource() {
                while (inputNames.hasNext() &&
                       (classes == null  ||  !classes.hasNext())) {
                    classes =
                        findResourceClasses(inputNames.nextResourceName());
                }
    
                return (classes != null  &&  classes.hasNext())
                       ? classes.nextResourceClass()
                       : null;
            }
        };
    }
}

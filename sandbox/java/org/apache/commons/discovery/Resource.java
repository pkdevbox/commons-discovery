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
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.discovery.log.DiscoveryLogFactory;
import org.apache.commons.discovery.resource.DiscoverResources;
import org.apache.commons.logging.Log;


/**
 * 'Resource' located by discovery.
 * Naming of methods becomes a real pain ('getClass()')
 * so I've patterned this after ClassLoader...
 * 
 * I think it works well as it will give users a point-of-reference.
 * 
 * @author Craig R. McClanahan
 * @author Costin Manolache
 * @author Richard A. Sitze
 */
public class Resource
{
    private static Log log = DiscoveryLogFactory.newLog(DiscoverResources.class);
    public static void setLog(Log _log) {
        log = _log;
    }
    protected final String      name;
    protected final URL         resource;
    protected final ClassLoader loader;

    public Resource(String resourceName, URL resource, ClassLoader loader) {
        this.name = resourceName;
        this.resource = resource;
        this.loader = loader;

        if (log.isDebugEnabled())
            log.debug("new " + this);
    }

    /**
     * Get the value of resourceName.
     * @return value of resourceName.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the value of URL.
     * @return value of URL.
     */
    public URL getResource() {
        return resource;
    }
    
    /**
     * Get the value of URL.
     * @return value of URL.
     */
    public InputStream getResourceAsStream() {
        try {
            return resource.openStream();
        } catch (IOException e) {
            return null;  // ignore
        }
    }
    
    /**
     * Get the value of loader.
     * @return value of loader.
     */
    public ClassLoader getClassLoader() {
        return loader ;
    }
}
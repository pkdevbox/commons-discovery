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

package org.apache.commons.discovery.types;


/**
 * Represents a Service Programming Interface (spi) context,
 * to include an spi and the Thread Context Class Loader for
 * the thread that created an instance of this object.
 * 
 * @author Richard A. Sitze
 */
public class SPInterface {
    /**
     * The service programming interface name
     */
    private final String name;
    
    /**
     * The service programming interface: intended to be
     * an interface or abstract class, but not limited
     * to those two.
     */        
    private final Class provider;
    
    /**
     * The property name to be used for finding the name of
     * the SPI implementation class.
     */
    private final String propertyName;
    
    
    private Class  paramClasses[] = null;
    private Object params[] = null;


    public SPInterface(Class provider) {
        this(provider, provider.getName());
    }
    
    public SPInterface(Class provider, String propertyName) {
        this.name = provider.getName();
        this.provider = provider;
        this.propertyName = propertyName;
    }

    public SPInterface(Class provider, Class paramClasses[], Object params[]) {
        this(provider, provider.getName(), paramClasses, params);
    }
    
    public SPInterface(Class provider, String propertyName, Class paramClasses[], Object params[]) {
        this.name = provider.getName();
        this.provider = provider;
        this.propertyName = propertyName;
        this.paramClasses = paramClasses;
        this.params = params;
    }
    
    public String getSPName() {
        return name;
    }
    
    public Class getSPClass() {
        return provider;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    public ImplClass createImplClass(String className) {
        return new ImplClass(className, paramClasses, params);
    }
}

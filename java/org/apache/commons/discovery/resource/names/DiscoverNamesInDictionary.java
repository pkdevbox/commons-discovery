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
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
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

package org.apache.commons.discovery.resource.names;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameListener;
import org.apache.commons.discovery.log.DiscoveryLogFactory;
import org.apache.commons.logging.Log;


/**
 * Recover resources from a Dictionary.  This covers Properties as well,
 * since <code>Properties extends Hashtable extends Dictionary</code>.
 * 
 * The recovered value is expected to be either a <code>String</code>
 * or a <code>String[]</code>.
 * 
 * @author Richard A. Sitze
 */
public class DiscoverNamesInDictionary extends ResourceNameDiscoverImpl
    implements ResourceNameDiscover, ResourceNameListener
{
    private static Log log = DiscoveryLogFactory.newLog(DiscoverNamesInDictionary.class);
    public static void setLog(Log _log) {
        log = _log;
    }

    private Dictionary dictionary;
    
    /** Construct a new resource discoverer
     */
    public DiscoverNamesInDictionary() {
        this(new Hashtable());
    }
    
    /** Construct a new resource discoverer
     */
    public DiscoverNamesInDictionary(Dictionary dictionary) {
        setDictionary(dictionary);
    }

    protected Dictionary getDictionary() {
        return dictionary;
    }

    /**
     * Specify set of class loaders to be used in searching.
     */
    public void setDictionary(Dictionary table) {
        this.dictionary = dictionary;
    }
    
    public void addResource(String resourceName, String resource) {
        dictionary.put(resourceName, resource);
    }
    
    public void addResource(String resourceName, String[] resources) {
        dictionary.put(resourceName, resources);
    }

    /**
     * @return Enumeration of ResourceInfo
     */
    public boolean find(String resourceName) {
        if (log.isDebugEnabled())
            log.debug("find: resourceName='" + resourceName + "'");

        Object baseResource = dictionary.get(resourceName);

        if (baseResource instanceof String) {
            return notifyListener((String)baseResource);
        } else if (baseResource instanceof String[]) {
            String[] resources = (String[])baseResource;
            for (int i = 0; i < resources.length; i++) {
                if (!notifyListener(resources[i])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean found(String resourceName) {
        return find(resourceName);
    }
}
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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.discovery.log.DiscoveryLogFactory;
import org.apache.commons.logging.Log;


/**
 * @author Richard A. Sitze
 */
public class ClassUtils {
    private static Log log = DiscoveryLogFactory.newLog(ClassUtils.class);
    public static void setLog(Log _log) {
        log = _log;
    }

    /**
     * Get package name.
     * Not all class loaders 'keep' package information,
     * in which case Class.getPackage() returns null.
     * This means that calling Class.getPackage().getName()
     * is unreliable at best.
     */
    public static String getPackageName(Class clazz) {
        Package clazzPackage = clazz.getPackage();
        String packageName;
        if (clazzPackage != null) {
            packageName = clazzPackage.getName();
        } else {
            String clazzName = clazz.getName();
            packageName = new String(clazzName.toCharArray(), 0, clazzName.lastIndexOf('.'));
        }
        return packageName;
    }
    
    /**
     * @return Method 'public static returnType methodName(paramTypes)',
     *         if found to be <strong>directly</strong> implemented by clazz.
     */
    public static Method findPublicStaticMethod(Class clazz,
                                                Class returnType,
                                                String methodName,
                                                Class[] paramTypes) {
        boolean problem = false;
        Method method = null;

        // verify '<methodName>(<paramTypes>)' is directly in class.
        try {
            method = clazz.getDeclaredMethod(methodName, paramTypes);
        } catch(NoSuchMethodException e) {
            problem = true;
            log.debug("Class " + clazz.getName() + ": missing method '" + methodName + "(...)", e);
        }
        
        // verify 'public static <returnType>'
        if (!problem  &&
            !(Modifier.isPublic(method.getModifiers()) &&
              Modifier.isStatic(method.getModifiers()) &&
              method.getReturnType() == returnType)) {
            if (log.isDebugEnabled()) {
                if (!Modifier.isPublic(method.getModifiers())) {
                    log.debug(methodName + "() is not public");
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    log.debug(methodName + "() is not static");
                }
                if (method.getReturnType() != returnType) {
                    log.debug("Method returns: " + method.getReturnType().getName() + "@@" + method.getReturnType().getClassLoader());
                    log.debug("Should return:  " + returnType.getName() + "@@" + returnType.getClassLoader());
                }
            }
            problem = true;
            method = null;
        }
        
        return method;
    }
}
/*
 * $Header: /home/cvs/jakarta-commons/logging/src/java/org/apache/commons/logging/impl/SimpleLog.java,v 1.4 2002/06/15 20:54:48 craigmcc Exp $
 * $Revision: 1.4 $
 * $Date: 2002/06/15 20:54:48 $
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


package org.apache.commons.discovery.log;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;


/**
 * <p>Simple implementation of Log that sends all enabled log messages,
 * for all defined loggers, to System.err.
 * </p>
 * 
 * <p>Hacked from commons-logging SimpleLog for use in discovery.
 * This is intended to be enough of a Log implementation to bootstrap
 * Discovery.
 * </p>
 * 
 * <p>One property: <code>org.apache.commons.discovery.log.level</code>.
 * valid values: all, trace, debug, info, warn, error, fatal, off.
 * </p>
 * 
 * @author Richard A. Sitze
 * @author <a href="mailto:sanders@apache.org">Scott Sanders</a>
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 *
 * @version $Id: SimpleLog.java,v 1.4 2002/06/15 20:54:48 craigmcc Exp $
 */
public class SimpleLog implements Log {
      // ---------------------------------------------------- Log Level Constants

    /** "Trace" level logging. */
    public static final int LOG_LEVEL_TRACE  = 1;
    /** "Debug" level logging. */
    public static final int LOG_LEVEL_DEBUG  = 2;
    /** "Info" level logging. */
    public static final int LOG_LEVEL_INFO   = 3;
    /** "Warn" level logging. */
    public static final int LOG_LEVEL_WARN   = 4;
    /** "Error" level logging. */
    public static final int LOG_LEVEL_ERROR  = 5;
    /** "Fatal" level logging. */
    public static final int LOG_LEVEL_FATAL  = 6;

    /** Enable all logging levels */
    public static final int LOG_LEVEL_ALL    = (LOG_LEVEL_TRACE - 1);

    /** Enable no logging levels */
    public static final int LOG_LEVEL_OFF    = (LOG_LEVEL_FATAL + 1);

    // ------------------------------------------------------- Class Attributes

    static protected final String PROP_LEVEL =
        "org.apache.commons.discovery.log.level";

    /** Include the instance name in the log message? */
    static protected boolean showLogName = false;

    /** Include the short name ( last component ) of the logger in the log
        message. Default to true - otherwise we'll be lost in a flood of
        messages without knowing who sends them.
    */
    static protected boolean showShortName = true;

    /** Include the current time in the log message */
    static protected boolean showDateTime = false;

    /** Used to format times */
    static protected DateFormat dateFormatter = null;

    /** The current log level */
    static protected int logLevel = LOG_LEVEL_INFO;
    
    
    /**
     * Use 'out' instead of 'err' for logging
     * to keep in-sync with test messages.
     */
    static private PrintStream out = System.out;

    // ------------------------------------------------------------ Initializer

    // initialize class attributes
    static {
        if(showDateTime) {
            dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
        }

        // set log level from properties
        String lvl = System.getProperty(PROP_LEVEL);

        if("all".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_ALL);
        } else if("trace".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_TRACE);
        } else if("debug".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_DEBUG);
        } else if("info".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_INFO);
        } else if("warn".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_WARN);
        } else if("error".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_ERROR);
        } else if("fatal".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_FATAL);
        } else if("off".equalsIgnoreCase(lvl)) {
            setLevel(SimpleLog.LOG_LEVEL_OFF);
        }
    }

    // -------------------------------------------------------- Properties

    /**
     * <p> Set logging level. </p>
     *
     * @param level new logging level
     */
    public static void setLevel(int currentLogLevel) {
        logLevel = currentLogLevel;
    }

    /**
     * <p> Get logging level. </p>
     */
    public static int getLevel() {
        return logLevel;
    }

    /**
     * Is the given log level currently enabled?
     *
     * @param logLevel is this level enabled?
     */
    protected static boolean isLevelEnabled(int level) {
        // log level are numerically ordered so can use simple numeric
        // comparison
        return (level >= getLevel());
    }



    // ------------------------------------------------------------- Attributes

    /** The name of this simple log instance */
    protected String logName = null;

    private String prefix=null;

    
    // ------------------------------------------------------------ Constructor
    
    /**
     * Construct a simple log with given name.
     *
     * @param name log name
     */
    public SimpleLog(String name) {
        logName = name;
    }


    // -------------------------------------------------------- Logging Methods


    /**
     * <p> Do the actual logging.
     * This method assembles the message
     * and then prints to <code>System.err</code>.</p>
     */
    protected void log(int type, Object message, Throwable t) {
        // use a string buffer for better performance
        StringBuffer buf = new StringBuffer();

        // append date-time if so configured
        if(showDateTime) {
            buf.append(dateFormatter.format(new Date()));
            buf.append(" ");
        }

        // append a readable representation of the log leve
        switch(type) {
            case SimpleLog.LOG_LEVEL_TRACE: buf.append("[TRACE] "); break;
            case SimpleLog.LOG_LEVEL_DEBUG: buf.append("[DEBUG] "); break;
            case SimpleLog.LOG_LEVEL_INFO:  buf.append("[INFO ] "); break;
            case SimpleLog.LOG_LEVEL_WARN:  buf.append("[WARN ] "); break;
            case SimpleLog.LOG_LEVEL_ERROR: buf.append("[ERROR] "); break;
            case SimpleLog.LOG_LEVEL_FATAL: buf.append("[FATAL] "); break;
        }

        // append the name of the log instance if so configured
     	if( showShortName) {
            if( prefix==null ) {
                // cut all but the last component of the name for both styles
                prefix = logName.substring( logName.lastIndexOf(".") +1) + " - ";
                prefix = prefix.substring( prefix.lastIndexOf("/") +1) + "-";
            }
            buf.append( prefix );
        } else if(showLogName) {
            buf.append(String.valueOf(logName)).append(" - ");
        }

        // append the message
        buf.append(String.valueOf(message));

        // append stack trace if not null
        if(t != null) {
            buf.append(" <");
            buf.append(t.toString());
            buf.append(">");
        }

        // print to System.err
        out.println(buf.toString());
        
        if (t != null)
            t.printStackTrace(System.err);
    }


    // -------------------------------------------------------- Log Implementation


    /**
     * <p> Log a message with debug log level.</p>
     */
    public final void debug(Object message) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_DEBUG)) {
            log(SimpleLog.LOG_LEVEL_DEBUG, message, null);
        }
    }


    /**
     * <p> Log an error with debug log level.</p>
     */
    public final void debug(Object message, Throwable t) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_DEBUG)) {
            log(SimpleLog.LOG_LEVEL_DEBUG, message, t);
        }
    }


    /**
     * <p> Log a message with debug log level.</p>
     */
    public final void trace(Object message) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_TRACE)) {
            log(SimpleLog.LOG_LEVEL_TRACE, message, null);
        }
    }


    /**
     * <p> Log an error with debug log level.</p>
     */
    public final void trace(Object message, Throwable t) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_TRACE)) {
            log(SimpleLog.LOG_LEVEL_TRACE, message, t);
        }
    }


    /**
     * <p> Log a message with info log level.</p>
     */
    public final void info(Object message) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_INFO)) {
            log(SimpleLog.LOG_LEVEL_INFO,message,null);
        }
    }


    /**
     * <p> Log an error with info log level.</p>
     */
    public final void info(Object message, Throwable t) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_INFO)) {
            log(SimpleLog.LOG_LEVEL_INFO, message, t);
        }
    }


    /**
     * <p> Log a message with warn log level.</p>
     */
    public final void warn(Object message) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_WARN)) {
            log(SimpleLog.LOG_LEVEL_WARN, message, null);
        }
    }


    /**
     * <p> Log an error with warn log level.</p>
     */
    public final void warn(Object message, Throwable t) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_WARN)) {
            log(SimpleLog.LOG_LEVEL_WARN, message, t);
        }
    }


    /**
     * <p> Log a message with error log level.</p>
     */
    public final void error(Object message) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_ERROR)) {
            log(SimpleLog.LOG_LEVEL_ERROR, message, null);
        }
    }


    /**
     * <p> Log an error with error log level.</p>
     */
    public final void error(Object message, Throwable t) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_ERROR)) {
            log(SimpleLog.LOG_LEVEL_ERROR, message, t);
        }
    }


    /**
     * <p> Log a message with fatal log level.</p>
     */
    public final void fatal(Object message) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_FATAL)) {
            log(SimpleLog.LOG_LEVEL_FATAL, message, null);
        }
    }


    /**
     * <p> Log an error with fatal log level.</p>
     */
    public final void fatal(Object message, Throwable t) {
        if (isLevelEnabled(SimpleLog.LOG_LEVEL_FATAL)) {
            log(SimpleLog.LOG_LEVEL_FATAL, message, t);
        }
    }


    /**
     * <p> Are debug messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isDebugEnabled() {
        return isLevelEnabled(SimpleLog.LOG_LEVEL_DEBUG);
    }


    /**
     * <p> Are error messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isErrorEnabled() {
        return isLevelEnabled(SimpleLog.LOG_LEVEL_ERROR);
    }


    /**
     * <p> Are fatal messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isFatalEnabled() {
        return isLevelEnabled(SimpleLog.LOG_LEVEL_FATAL);
    }


    /**
     * <p> Are info messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isInfoEnabled() {
        return isLevelEnabled(SimpleLog.LOG_LEVEL_INFO);
    }


    /**
     * <p> Are trace messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isTraceEnabled() {
        return isLevelEnabled(SimpleLog.LOG_LEVEL_TRACE);
    }


    /**
     * <p> Are warn messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isWarnEnabled() {
        return isLevelEnabled(SimpleLog.LOG_LEVEL_WARN);
    }
}
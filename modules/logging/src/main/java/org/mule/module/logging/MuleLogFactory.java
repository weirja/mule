/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.impl.MuleLocationAwareLog;
import org.apache.commons.logging.impl.MuleLog;
import org.apache.commons.logging.impl.SLF4JLogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * TODO release/flush logger repositories on app removal
 */
public class MuleLogFactory extends SLF4JLogFactory
{
    protected ConcurrentHashMap<Integer, ConcurrentMap<String, Log>> repository = new ConcurrentHashMap<Integer, ConcurrentMap<String, Log>>();

    protected static final Integer NO_CCL_CLASSLOADER = 0;

    public Log getInstance(String name) throws LogConfigurationException
    {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        ConcurrentMap<String, Log> loggerMap = repository.get(ccl == null ? NO_CCL_CLASSLOADER : ccl.hashCode());

        if (loggerMap == null)
        {
            loggerMap = new ConcurrentHashMap<String, Log>();

            final ConcurrentMap<String, Log> previous = repository.putIfAbsent(ccl == null ? NO_CCL_CLASSLOADER : ccl.hashCode(), loggerMap);
            if (previous != null)
            {
                loggerMap = previous;
            }
        }

        Log instance = loggerMap.get(name);

        if (instance == null)
        {
            Logger logger = LoggerFactory.getLogger(name);
            if (logger instanceof LocationAwareLogger)
            {
                instance = new MuleLocationAwareLog((LocationAwareLogger) logger);
            }
            else
            {
                instance = new MuleLog(logger);
            }
            final Log previous = loggerMap.putIfAbsent(name, instance);
            if (previous != null)
            {
                // someone got there before us
                instance = previous;
            }
        }

        return instance;
    }
}

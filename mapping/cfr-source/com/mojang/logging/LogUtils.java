/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.core.LifeCycle
 *  org.apache.logging.log4j.core.LoggerContext
 *  org.apache.logging.log4j.core.config.Configuration
 *  org.apache.logging.log4j.core.config.LoggerConfig
 *  org.apache.logging.log4j.spi.LoggerContext
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.slf4j.Marker
 *  org.slf4j.MarkerFactory
 *  org.slf4j.event.Level
 */
package com.mojang.logging;

import java.util.function.Supplier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogUtils {
    public static final String FATAL_MARKER_ID = "FATAL";
    public static final Marker FATAL_MARKER = MarkerFactory.getMarker((String)"FATAL");
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static boolean isLoggerActive() {
        org.apache.logging.log4j.spi.LoggerContext loggerContext = LogManager.getContext();
        if (loggerContext instanceof LifeCycle) {
            LifeCycle lifeCycle = (LifeCycle)loggerContext;
            return !lifeCycle.isStopped();
        }
        return true;
    }

    public static void configureRootLoggingLevel(org.slf4j.event.Level level) {
        LoggerContext loggerContext = (LoggerContext)LogManager.getContext((boolean)false);
        Configuration configuration = loggerContext.getConfiguration();
        LoggerConfig loggerConfig = configuration.getLoggerConfig("");
        loggerConfig.setLevel(LogUtils.convertLevel(level));
        loggerContext.updateLoggers();
    }

    private static Level convertLevel(org.slf4j.event.Level level) {
        return switch (level) {
            default -> throw new IncompatibleClassChangeError();
            case org.slf4j.event.Level.INFO -> Level.INFO;
            case org.slf4j.event.Level.WARN -> Level.WARN;
            case org.slf4j.event.Level.DEBUG -> Level.DEBUG;
            case org.slf4j.event.Level.ERROR -> Level.ERROR;
            case org.slf4j.event.Level.TRACE -> Level.TRACE;
        };
    }

    public static Object defer(Supplier<Object> supplier) {
        class ToString {
            final /* synthetic */ Supplier val$result;

            ToString(Supplier supplier) {
                this.val$result = supplier;
            }

            public String toString() {
                return this.val$result.get().toString();
            }
        }
        return new ToString(supplier);
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger(STACK_WALKER.getCallerClass());
    }
}


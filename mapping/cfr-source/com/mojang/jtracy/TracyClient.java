/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

import com.mojang.jtracy.ContinuousFrame;
import com.mojang.jtracy.DiscontinuousFrame;
import com.mojang.jtracy.GpuApi;
import com.mojang.jtracy.GpuContext;
import com.mojang.jtracy.Loader;
import com.mojang.jtracy.MemoryPool;
import com.mojang.jtracy.Plot;
import com.mojang.jtracy.TracyBindings;
import com.mojang.jtracy.Zone;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class TracyClient {
    private static boolean loaded = false;
    private static AtomicInteger lastGpuContextId = new AtomicInteger(0);

    public static boolean isAvailable() {
        return loaded;
    }

    public static synchronized void load() throws UnsatisfiedLinkError {
        if (!loaded) {
            new Loader().load();
            loaded = true;
        }
    }

    public static void markFrame() {
        if (loaded) {
            TracyBindings.markFrame(0L);
        }
    }

    public static void frameImage(ByteBuffer byteBuffer, int n, int n2, int n3, boolean bl) {
        if (loaded) {
            TracyBindings.frameImage(byteBuffer, n, n2, n3, bl);
        }
    }

    public static Zone beginZone(String string, boolean bl) {
        if (loaded) {
            StackWalker stackWalker;
            Optional optional;
            String string2 = "";
            String string3 = "";
            int n = 0;
            if (bl && (optional = (stackWalker = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 2)).walk(stream -> stream.filter(stackFrame -> stackFrame.getDeclaringClass() != TracyClient.class).findFirst())).isPresent()) {
                StackWalker.StackFrame stackFrame = (StackWalker.StackFrame)optional.get();
                string2 = stackFrame.getMethodName();
                string3 = stackFrame.getFileName();
                n = stackFrame.getLineNumber();
            }
            return new Zone(TracyBindings.beginZone(string, string2, string3, n));
        }
        return Zone.UNAVAILABLE;
    }

    public static Zone beginZone(String string, String string2, String string3, int n) {
        if (loaded) {
            return new Zone(TracyBindings.beginZone(string, string2, string3, n));
        }
        return Zone.UNAVAILABLE;
    }

    public static void setThreadName(String string, int n) {
        if (loaded) {
            TracyBindings.setThreadName(string, n);
        }
    }

    public static Plot createPlot(String string) {
        if (loaded) {
            return new Plot(TracyBindings.leakName(string));
        }
        return Plot.UNAVAILABLE;
    }

    public static DiscontinuousFrame createDiscontinuousFrame(String string) {
        if (loaded) {
            return new DiscontinuousFrame(TracyBindings.leakName(string));
        }
        return DiscontinuousFrame.UNAVAILABLE;
    }

    public static ContinuousFrame createContinuousFrame(String string) {
        if (loaded) {
            return new ContinuousFrame(TracyBindings.leakName(string));
        }
        return ContinuousFrame.UNAVAILABLE;
    }

    public static MemoryPool createMemoryPool(String string) {
        if (loaded) {
            return new MemoryPool(TracyBindings.leakName(string));
        }
        return MemoryPool.UNAVAILABLE;
    }

    public static void reportAppInfo(String string) {
        if (loaded) {
            TracyBindings.appInfo(string);
        }
    }

    public static void message(String string) {
        if (loaded) {
            TracyBindings.message(string);
        }
    }

    public static void message(String string, int n) {
        if (loaded) {
            TracyBindings.messageColored(string, n);
        }
    }

    public static void message(Supplier<String> supplier) {
        if (loaded) {
            TracyBindings.message(supplier.get());
        }
    }

    public static void message(Supplier<String> supplier, int n) {
        if (loaded) {
            TracyBindings.messageColored(supplier.get(), n);
        }
    }

    public static GpuContext createGpuContext(GpuApi gpuApi, long l, float f) {
        if (loaded) {
            int n = lastGpuContextId.incrementAndGet();
            if (n == 255) {
                throw new UnsupportedOperationException("Too many GPU contexts were created");
            }
            TracyBindings.newGpuContext(n, l, f, 0, gpuApi.getId());
            return new GpuContext(n);
        }
        return GpuContext.UNAVAILABLE;
    }
}


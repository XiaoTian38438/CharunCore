/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

public record Stopwatch(long creationTime, long accumulatedElapsedTime) {
    public Stopwatch(long l) {
        this(l, 0L);
    }

    public long elapsedMilliseconds(long l) {
        long l2 = l - this.creationTime;
        return this.accumulatedElapsedTime + l2;
    }

    public double elapsedSeconds(long l) {
        return (double)this.elapsedMilliseconds(l) / 1000.0;
    }
}


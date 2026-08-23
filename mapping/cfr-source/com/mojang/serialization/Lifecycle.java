/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization;

public class Lifecycle {
    private static final Lifecycle STABLE = new Lifecycle(){

        public String toString() {
            return "Stable";
        }
    };
    private static final Lifecycle EXPERIMENTAL = new Lifecycle(){

        public String toString() {
            return "Experimental";
        }
    };

    private Lifecycle() {
    }

    public static Lifecycle experimental() {
        return EXPERIMENTAL;
    }

    public static Lifecycle stable() {
        return STABLE;
    }

    public static Lifecycle deprecated(int n) {
        return new Deprecated(n);
    }

    public Lifecycle add(Lifecycle lifecycle) {
        if (this == EXPERIMENTAL || lifecycle == EXPERIMENTAL) {
            return EXPERIMENTAL;
        }
        if (this instanceof Deprecated) {
            if (lifecycle instanceof Deprecated && ((Deprecated)lifecycle).since < ((Deprecated)this).since) {
                return lifecycle;
            }
            return this;
        }
        if (lifecycle instanceof Deprecated) {
            return lifecycle;
        }
        return STABLE;
    }

    public static final class Deprecated
    extends Lifecycle {
        private final int since;

        public Deprecated(int n) {
            this.since = n;
        }

        public int since() {
            return this.since;
        }
    }
}


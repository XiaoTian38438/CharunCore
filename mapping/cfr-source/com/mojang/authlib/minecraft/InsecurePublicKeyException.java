/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.authlib.minecraft;

public class InsecurePublicKeyException
extends RuntimeException {
    public InsecurePublicKeyException(String string) {
        super(string);
    }

    public static class InvalidException
    extends InsecurePublicKeyException {
        public InvalidException(String string) {
            super(string);
        }
    }

    public static class MissingException
    extends InsecurePublicKeyException {
        public MissingException(String string) {
            super(string);
        }
    }
}


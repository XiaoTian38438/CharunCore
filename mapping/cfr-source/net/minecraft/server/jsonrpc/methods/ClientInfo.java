/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.jsonrpc.methods;

public record ClientInfo(Integer connectionId) {
    public static ClientInfo of(Integer n) {
        return new ClientInfo(n);
    }
}


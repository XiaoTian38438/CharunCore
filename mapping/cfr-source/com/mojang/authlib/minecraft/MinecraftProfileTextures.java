/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.authlib.minecraft;

import com.mojang.authlib.SignatureState;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import javax.annotation.Nullable;

public record MinecraftProfileTextures(@Nullable MinecraftProfileTexture skin, @Nullable MinecraftProfileTexture cape, @Nullable MinecraftProfileTexture elytra, SignatureState signatureState) {
    public static final MinecraftProfileTextures EMPTY = new MinecraftProfileTextures(null, null, null, SignatureState.SIGNED);
}


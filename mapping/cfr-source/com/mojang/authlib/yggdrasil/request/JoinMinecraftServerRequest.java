/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.mojang.authlib.yggdrasil.request;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;

public record JoinMinecraftServerRequest(@SerializedName(value="accessToken") String accessToken, @SerializedName(value="selectedProfile") UUID selectedProfile, @SerializedName(value="serverId") String serverId) {
}


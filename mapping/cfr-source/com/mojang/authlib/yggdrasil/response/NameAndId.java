/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.mojang.authlib.yggdrasil.response;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;

public record NameAndId(@SerializedName(value="id") UUID id, @SerializedName(value="name") String name) {
}


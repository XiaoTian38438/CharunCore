/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.mojang.authlib.yggdrasil.response;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.yggdrasil.ProfileActionType;

public record ProfileAction(@SerializedName(value="action") ProfileActionType type) {
}


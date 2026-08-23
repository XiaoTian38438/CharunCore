/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.mojang.authlib.minecraft.report;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;

public record ReportedEntity(@SerializedName(value="profileId") UUID profileId) {
}


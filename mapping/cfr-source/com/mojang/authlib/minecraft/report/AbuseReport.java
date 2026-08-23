/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  javax.annotation.Nullable
 */
package com.mojang.authlib.minecraft.report;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.minecraft.report.ReportEvidence;
import com.mojang.authlib.minecraft.report.ReportedEntity;
import java.time.Instant;
import javax.annotation.Nullable;

public record AbuseReport(@SerializedName(value="opinionComments") String opinionComments, @SerializedName(value="reason") @Nullable String reason, @SerializedName(value="evidence") @Nullable ReportEvidence evidence, @SerializedName(value="skinUrl") @Nullable String skinUrl, @SerializedName(value="reportedEntity") ReportedEntity reportedEntity, @SerializedName(value="createdTime") Instant createdTime) {
    public static AbuseReport name(String string, ReportedEntity reportedEntity, Instant instant) {
        return new AbuseReport(string, null, null, null, reportedEntity, instant);
    }

    public static AbuseReport skin(String string, String string2, @Nullable String string3, ReportedEntity reportedEntity, Instant instant) {
        return new AbuseReport(string, string2, null, string3, reportedEntity, instant);
    }

    public static AbuseReport chat(String string, String string2, ReportEvidence reportEvidence, ReportedEntity reportedEntity, Instant instant) {
        return new AbuseReport(string, string2, reportEvidence, null, reportedEntity, instant);
    }
}


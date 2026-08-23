/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  javax.annotation.Nullable
 *  org.apache.commons.io.FilenameUtils
 *  org.apache.commons.lang3.builder.ToStringBuilder
 */
package com.mojang.authlib.minecraft;

import com.google.gson.annotations.SerializedName;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MinecraftProfileTexture {
    public static final int PROFILE_TEXTURE_COUNT = Type.values().length;
    @SerializedName(value="url")
    private final String url;
    @SerializedName(value="metadata")
    private final Map<String, String> metadata;

    public MinecraftProfileTexture(String string, Map<String, String> map) {
        this.url = string;
        this.metadata = map;
    }

    public String getUrl() {
        return this.url;
    }

    @Nullable
    public String getMetadata(String string) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(string);
    }

    public String getHash() {
        try {
            return FilenameUtils.getBaseName((String)new URL(this.url).getPath());
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException("Invalid profile texture url");
        }
    }

    public String toString() {
        return new ToStringBuilder((Object)this).append("url", (Object)this.url).append("hash", (Object)this.getHash()).toString();
    }

    public static enum Type {
        SKIN,
        CAPE,
        ELYTRA;

    }
}


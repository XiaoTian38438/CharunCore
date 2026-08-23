/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.authlib.yggdrasil;

import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TextureUrlChecker {
    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");
    private static final List<String> ALLOWED_DOMAINS = List.of(".minecraft.net", ".mojang.com");
    private static final List<String> BLOCKED_DOMAINS = List.of("bugs.mojang.com", "education.minecraft.net", "feedback.minecraft.net");

    public static boolean isAllowedTextureDomain(String string) {
        URI uRI;
        try {
            uRI = new URI(string).normalize();
        }
        catch (URISyntaxException uRISyntaxException) {
            return false;
        }
        String string2 = uRI.getScheme();
        if (string2 == null || !ALLOWED_SCHEMES.contains(string2)) {
            return false;
        }
        String string3 = uRI.getHost();
        if (string3 == null) {
            return false;
        }
        String string4 = IDN.toUnicode(string3);
        String string5 = string4.toLowerCase(Locale.ROOT);
        if (!string5.equals(string4)) {
            return false;
        }
        return TextureUrlChecker.isDomainOnList(string4, ALLOWED_DOMAINS) && !TextureUrlChecker.isDomainOnList(string4, BLOCKED_DOMAINS);
    }

    private static boolean isDomainOnList(String string, List<String> list) {
        for (String string2 : list) {
            if (!string.endsWith(string2)) continue;
            return true;
        }
        return false;
    }
}


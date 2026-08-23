/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package com.mojang.authlib;

import com.mojang.authlib.AuthenticationService;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.lang3.Validate;

public abstract class HttpAuthenticationService
implements AuthenticationService {
    private final Proxy proxy;

    protected HttpAuthenticationService(Proxy proxy) {
        Validate.notNull((Object)proxy);
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public static URL constantURL(String string) {
        try {
            return new URL(string);
        }
        catch (MalformedURLException malformedURLException) {
            throw new Error("Couldn't create constant for " + string, malformedURLException);
        }
    }

    public static String buildQuery(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            if (entry.getValue() == null) continue;
            stringBuilder.append('=');
            stringBuilder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return stringBuilder.toString();
    }

    public static URL concatenateURL(URL uRL, String string) {
        try {
            if (uRL.getQuery() != null && uRL.getQuery().length() > 0) {
                return new URL(uRL.getProtocol(), uRL.getHost(), uRL.getPort(), uRL.getFile() + "&" + string);
            }
            return new URL(uRL.getProtocol(), uRL.getHost(), uRL.getPort(), uRL.getFile() + "?" + string);
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", malformedURLException);
        }
    }
}


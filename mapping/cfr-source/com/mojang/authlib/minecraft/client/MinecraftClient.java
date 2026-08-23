/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.Validate
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.authlib.minecraft.client;

import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import com.mojang.authlib.yggdrasil.response.ErrorResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftClient.class);
    public static final int CONNECT_TIMEOUT_MS = 5000;
    public static final int READ_TIMEOUT_MS = 5000;
    @Nullable
    private final String accessToken;
    private final Proxy proxy;
    private final ObjectMapper objectMapper = ObjectMapper.create();

    public MinecraftClient(@Nullable String string, Proxy proxy) {
        this.accessToken = string;
        this.proxy = (Proxy)Validate.notNull((Object)proxy);
    }

    public static MinecraftClient unauthenticated(Proxy proxy) {
        return new MinecraftClient(null, proxy);
    }

    @Nullable
    public <T> T get(URL uRL, Class<T> clazz) {
        Validate.notNull((Object)uRL);
        Validate.notNull(clazz);
        HttpURLConnection httpURLConnection = this.createUrlConnection(uRL);
        if (this.accessToken != null) {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.accessToken);
        }
        return this.readInputStream(uRL, clazz, httpURLConnection);
    }

    @Nullable
    public <T> T post(URL uRL, Class<T> clazz) {
        Validate.notNull((Object)uRL);
        Validate.notNull(clazz);
        HttpURLConnection httpURLConnection = this.postInternal(uRL, new byte[0]);
        return this.readInputStream(uRL, clazz, httpURLConnection);
    }

    @Nullable
    public <T> T post(URL uRL, Object object, Class<T> clazz) {
        Validate.notNull((Object)uRL);
        Validate.notNull((Object)object);
        Validate.notNull(clazz);
        String string = this.objectMapper.writeValueAsString(object);
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection httpURLConnection = this.postInternal(uRL, byArray);
        return this.readInputStream(uRL, clazz, httpURLConnection);
    }

    @Nullable
    private <T> T readInputStream(URL uRL, Class<T> clazz, HttpURLConnection httpURLConnection) {
        int n;
        InputStream inputStream;
        block8: {
            String string;
            block9: {
                inputStream = null;
                n = httpURLConnection.getResponseCode();
                if (n >= 400) break block8;
                inputStream = httpURLConnection.getInputStream();
                string = IOUtils.toString((InputStream)inputStream, (Charset)StandardCharsets.UTF_8);
                if (!string.isEmpty()) break block9;
                T t = null;
                IOUtils.closeQuietly((InputStream)inputStream);
                return t;
            }
            T t = this.objectMapper.readValue(string, clazz);
            IOUtils.closeQuietly((InputStream)inputStream);
            return t;
        }
        try {
            try {
                String string = httpURLConnection.getContentType();
                inputStream = httpURLConnection.getErrorStream();
                if (inputStream != null) {
                    String string2 = IOUtils.toString((InputStream)inputStream, (Charset)StandardCharsets.UTF_8);
                    if (string != null && string.startsWith("text/html")) {
                        LOGGER.error("Got an error with a html body connecting to {}: {}", (Object)uRL.toString(), (Object)string2);
                        throw new MinecraftClientHttpException(n);
                    }
                    ErrorResponse errorResponse = this.objectMapper.readValue(string2, ErrorResponse.class);
                    throw new MinecraftClientHttpException(n, errorResponse);
                }
                throw new MinecraftClientHttpException(n);
            }
            catch (IOException iOException) {
                throw new MinecraftClientException(MinecraftClientException.ErrorType.SERVICE_UNAVAILABLE, "Failed to read from " + String.valueOf(uRL) + " due to " + iOException.getMessage(), iOException);
            }
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(inputStream);
            throw throwable;
        }
    }

    private HttpURLConnection postInternal(URL uRL, byte[] byArray) {
        HttpURLConnection httpURLConnection = this.createUrlConnection(uRL);
        OutputStream outputStream = null;
        try {
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty("Content-Length", "" + byArray.length);
            if (this.accessToken != null) {
                httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.accessToken);
            }
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            outputStream = httpURLConnection.getOutputStream();
            IOUtils.write((byte[])byArray, (OutputStream)outputStream);
        }
        catch (IOException iOException) {
            try {
                throw new MinecraftClientException(MinecraftClientException.ErrorType.SERVICE_UNAVAILABLE, "Failed to POST " + String.valueOf(uRL), iOException);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(outputStream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((OutputStream)outputStream);
        return httpURLConnection;
    }

    private HttpURLConnection createUrlConnection(URL uRL) {
        try {
            LOGGER.debug("Connecting to {}", (Object)uRL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection(this.proxy);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setUseCaches(false);
            return httpURLConnection;
        }
        catch (IOException iOException) {
            throw new MinecraftClientException(MinecraftClientException.ErrorType.SERVICE_UNAVAILABLE, "Failed connecting to " + String.valueOf(uRL), iOException);
        }
    }
}


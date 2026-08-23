/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.authlib.exceptions;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

public class MinecraftClientException
extends RuntimeException {
    protected final ErrorType type;

    protected MinecraftClientException(ErrorType errorType, String string) {
        super(string);
        this.type = errorType;
    }

    public MinecraftClientException(ErrorType errorType, String string, Throwable throwable) {
        super(string, throwable);
        this.type = errorType;
    }

    public ErrorType getType() {
        return this.type;
    }

    public AuthenticationException toAuthenticationException() {
        if (this.type == ErrorType.SERVICE_UNAVAILABLE) {
            return new AuthenticationUnavailableException();
        }
        return new AuthenticationException(this);
    }

    public static enum ErrorType {
        SERVICE_UNAVAILABLE,
        HTTP_ERROR,
        JSON_ERROR;

    }
}


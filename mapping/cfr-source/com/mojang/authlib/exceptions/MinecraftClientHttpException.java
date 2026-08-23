/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.StringUtils
 */
package com.mojang.authlib.exceptions;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.ForcedUsernameChangeException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.exceptions.UserBannedException;
import com.mojang.authlib.yggdrasil.response.ErrorResponse;
import java.util.Optional;
import java.util.StringJoiner;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class MinecraftClientHttpException
extends MinecraftClientException {
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    private final int status;
    @Nullable
    private final ErrorResponse response;

    public MinecraftClientHttpException(int n) {
        super(MinecraftClientException.ErrorType.HTTP_ERROR, MinecraftClientHttpException.getErrorMessage(n, null));
        this.status = n;
        this.response = null;
    }

    public MinecraftClientHttpException(int n, ErrorResponse errorResponse) {
        super(MinecraftClientException.ErrorType.HTTP_ERROR, MinecraftClientHttpException.getErrorMessage(n, errorResponse));
        this.status = n;
        this.response = errorResponse;
    }

    public int getStatus() {
        return this.status;
    }

    public Optional<ErrorResponse> getResponse() {
        return Optional.ofNullable(this.response);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MinecraftClientHttpException.class.getSimpleName() + "[", "]").add("type=" + String.valueOf((Object)this.type)).add("status=" + this.status).add("response=" + String.valueOf(this.response)).toString();
    }

    @Override
    public AuthenticationException toAuthenticationException() {
        if (this.hasError("ForbiddenOperationException")) {
            return new InvalidCredentialsException(this.getMessage());
        }
        if (this.hasError("multiplayer.access.banned")) {
            return new UserBannedException();
        }
        if (this.hasError("FORCED_USERNAME_CHANGE")) {
            return new ForcedUsernameChangeException();
        }
        if (this.hasError("InsufficientPrivilegesException")) {
            return new InsufficientPrivilegesException(this.getMessage(), this);
        }
        if (this.status == 401) {
            return new InvalidCredentialsException(this.getMessage(), this);
        }
        if (this.status >= 500) {
            return new AuthenticationUnavailableException(this.getMessage(), this);
        }
        return new AuthenticationException(this.getMessage(), this);
    }

    private Optional<String> getError() {
        return this.getResponse().map(ErrorResponse::error).filter(StringUtils::isNotEmpty);
    }

    private static String getErrorMessage(int n, ErrorResponse errorResponse) {
        Object object = errorResponse != null ? (StringUtils.isNotEmpty((CharSequence)errorResponse.errorMessage()) ? errorResponse.errorMessage() : (StringUtils.isNotEmpty((CharSequence)errorResponse.error()) ? errorResponse.error() : "Status: " + n)) : "Status: " + n;
        return object;
    }

    private boolean hasError(String string) {
        return this.getError().filter(string2 -> string2.equalsIgnoreCase(string)).isPresent();
    }
}


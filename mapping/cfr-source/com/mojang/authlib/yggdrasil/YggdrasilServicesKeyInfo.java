/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  com.google.gson.annotations.SerializedName
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.authlib.yggdrasil;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.ServicesKeyInfo;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilServicesKeyInfo
implements ServicesKeyInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilServicesKeyInfo.class);
    private static final ScheduledExecutorService FETCHER_EXECUTOR = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("Yggdrasil Key Fetcher").setDaemon(true).build());
    private static final int KEY_SIZE_BITS = 4096;
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final int REFRESH_INTERVAL_HOURS = 24;
    private static final int BASE_FAILURE_INTERVAL_MINUTES = 5;
    private static final int MAX_BACKOFF_EXPONENT = 6;
    private final PublicKey publicKey;

    private YggdrasilServicesKeyInfo(PublicKey publicKey) {
        this.publicKey = publicKey;
        String string = publicKey.getAlgorithm();
        if (!string.equals(KEY_ALGORITHM)) {
            throw new IllegalArgumentException("Expected RSA key, got " + string);
        }
    }

    public static ServicesKeyInfo parse(byte[] byArray) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byArray);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            return new YggdrasilServicesKeyInfo(publicKey);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException generalSecurityException) {
            throw new IllegalArgumentException("Invalid yggdrasil public key!", generalSecurityException);
        }
    }

    private static List<ServicesKeyInfo> parseList(@Nullable List<KeyData> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream().map(keyData -> YggdrasilServicesKeyInfo.parse(keyData.publicKey.array())).toList();
    }

    public static ServicesKeySet get(final URL uRL, final MinecraftClient minecraftClient) {
        final CompletableFuture completableFuture = new CompletableFuture();
        final AtomicReference atomicReference = new AtomicReference();
        FETCHER_EXECUTOR.execute(new Runnable(){
            private final AtomicInteger failureCount = new AtomicInteger();

            @Override
            public void run() {
                YggdrasilServicesKeyInfo.fetch(uRL, minecraftClient).ifPresent(atomicReference::set);
                completableFuture.complete(null);
                this.reschedule();
            }

            private void reschedule() {
                if (atomicReference.get() == null) {
                    int n = Math.min(this.failureCount.getAndIncrement(), 6);
                    int n2 = 5 * (1 << n);
                    FETCHER_EXECUTOR.schedule(this, (long)n2, TimeUnit.MINUTES);
                    return;
                }
                FETCHER_EXECUTOR.schedule(this, 24L, TimeUnit.HOURS);
            }
        });
        return ServicesKeySet.lazy(() -> {
            completableFuture.join();
            return Objects.requireNonNullElse((ServicesKeySet)atomicReference.get(), ServicesKeySet.EMPTY);
        });
    }

    private static Optional<ServicesKeySet> fetch(URL uRL, MinecraftClient minecraftClient) {
        KeySetResponse keySetResponse;
        try {
            keySetResponse = minecraftClient.get(uRL, KeySetResponse.class);
        }
        catch (MinecraftClientException minecraftClientException) {
            LOGGER.error("Failed to request yggdrasil public key", (Throwable)minecraftClientException);
            return Optional.empty();
        }
        if (keySetResponse == null) {
            return Optional.empty();
        }
        try {
            List<ServicesKeyInfo> list = YggdrasilServicesKeyInfo.parseList(keySetResponse.profilePropertyKeys);
            List<ServicesKeyInfo> list2 = YggdrasilServicesKeyInfo.parseList(keySetResponse.playerCertificateKeys);
            return Optional.of(servicesKeyType -> switch (servicesKeyType) {
                default -> throw new IncompatibleClassChangeError();
                case ServicesKeyType.PROFILE_PROPERTY -> list;
                case ServicesKeyType.PROFILE_KEY -> list2;
            });
        }
        catch (Exception exception) {
            LOGGER.error("Received malformed yggdrasil public key data", (Throwable)exception);
            return Optional.empty();
        }
    }

    @Override
    public Signature signature() {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(this.publicKey);
            return signature;
        }
        catch (InvalidKeyException | NoSuchAlgorithmException generalSecurityException) {
            throw new AssertionError("Failed to create signature", generalSecurityException);
        }
    }

    @Override
    public int keyBitCount() {
        return 4096;
    }

    @Override
    public boolean validateProperty(Property property) {
        byte[] byArray;
        Signature signature = this.signature();
        try {
            byArray = Base64.getDecoder().decode(property.signature());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error("Malformed signature encoding on property {}", (Object)property, (Object)illegalArgumentException);
            return false;
        }
        try {
            signature.update(property.value().getBytes());
            return signature.verify(byArray);
        }
        catch (SignatureException signatureException) {
            LOGGER.error("Failed to verify signature on property {}", (Object)property, (Object)signatureException);
            return false;
        }
    }

    private record KeySetResponse(@SerializedName(value="profilePropertyKeys") @Nullable List<KeyData> profilePropertyKeys, @SerializedName(value="playerCertificateKeys") @Nullable List<KeyData> playerCertificateKeys) {
    }

    private record KeyData(@SerializedName(value="publicKey") ByteBuffer publicKey) {
    }
}


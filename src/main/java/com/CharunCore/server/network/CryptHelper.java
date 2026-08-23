package com.CharunCore.server.network;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class CryptHelper {

    private CryptHelper() {}

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("RSA 密钥对生成失败", e);
        }
    }

    public static byte[] encodePublicKey(PublicKey key) {
        return key.getEncoded();
    }

    public static PublicKey decodePublicKey(byte[] encoded) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encoded));
        } catch (Exception e) {
            throw new IllegalStateException("公钥解析失败", e);
        }
    }

    public static String serverIdHash(byte[] publicKey, byte[] secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update("".getBytes(java.nio.charset.StandardCharsets.ISO_8859_1));
            digest.update(secret);
            digest.update(publicKey);
            return hexDigest(digest.digest());
        } catch (Exception e) {
            throw new IllegalStateException("SHA-1 计算失败", e);
        }
    }

    private static String hexDigest(byte[] digest) {
        return new java.math.BigInteger(digest).toString(16);
    }
}

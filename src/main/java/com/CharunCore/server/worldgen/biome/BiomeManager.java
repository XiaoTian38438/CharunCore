package com.CharunCore.server.worldgen.biome;

import java.security.MessageDigest;

public final class BiomeManager {

    private static final int ZOOM_BITS = 2;
    private static final int ZOOM = 4;
    private static final int ZOOM_MASK = 3;

    private final BiomeResolver noiseBiomeSource;
    private final long biomeZoomSeed;

    public BiomeManager(BiomeResolver noiseBiomeSource, long biomeZoomSeed) {
        this.noiseBiomeSource = noiseBiomeSource;
        this.biomeZoomSeed = biomeZoomSeed;
    }

    public static long obfuscateSeed(long seed) {
        byte[] le = new byte[8];
        for (int i = 0; i < 8; i++) {
            le[i] = (byte) (seed >>> (i * 8));
        }
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("SHA-256").digest(le);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | (hash[i] & 0xFF);
        }
        return result;
    }

    public BiomeManager withDifferentSource(BiomeResolver resolver) {
        return new BiomeManager(resolver, biomeZoomSeed);
    }

    public int getBiome(int x, int y, int z) {
        int x2 = x - 2;
        int y2 = y - 2;
        int z2 = z - 2;
        int qx = x2 >> ZOOM_BITS;
        int qy = y2 >> ZOOM_BITS;
        int qz = z2 >> ZOOM_BITS;
        double dx = (double) (x2 & ZOOM_MASK) / (double) ZOOM;
        double dy = (double) (y2 & ZOOM_MASK) / (double) ZOOM;
        double dz = (double) (z2 & ZOOM_MASK) / (double) ZOOM;

        int bestCorner = 0;
        double bestDist = Double.POSITIVE_INFINITY;
        for (int corner = 0; corner < 8; corner++) {
            boolean bitX = (corner & 4) == 0;
            boolean bitY = (corner & 2) == 0;
            boolean bitZ = (corner & 1) == 0;
            int cx = bitX ? qx : qx + 1;
            int cy = bitY ? qy : qy + 1;
            int cz = bitZ ? qz : qz + 1;
            double fx = bitX ? dx : dx - 1.0;
            double fy = bitY ? dy : dy - 1.0;
            double fz = bitZ ? dz : dz - 1.0;
            double dist = getFiddledDistance(biomeZoomSeed, cx, cy, cz, fx, fy, fz);
            if (bestDist > dist) {
                bestCorner = corner;
                bestDist = dist;
            }
        }
        int cx = (bestCorner & 4) == 0 ? qx : qx + 1;
        int cy = (bestCorner & 2) == 0 ? qy : qy + 1;
        int cz = (bestCorner & 1) == 0 ? qz : qz + 1;
        return noiseBiomeSource.getNoiseBiome(cx, cy, cz);
    }

    private static double getFiddledDistance(long seed, int x, int y, int z,
                                              double dx, double dy, double dz) {
        long l = seed;
        l = lcgNext(l, x);
        l = lcgNext(l, y);
        l = lcgNext(l, z);
        l = lcgNext(l, x);
        l = lcgNext(l, y);
        l = lcgNext(l, z);
        double fx = getFiddle(l);
        l = lcgNext(l, seed);
        double fy = getFiddle(l);
        l = lcgNext(l, seed);
        double fz = getFiddle(l);
        return (dz + fz) * (dz + fz) + (dy + fy) * (dy + fy) + (dx + fx) * (dx + fx);
    }

    private static double getFiddle(long l) {
        double d = (double) Math.floorMod(l >> 24, 1024) / 1024.0;
        return (d - 0.5) * 0.9;
    }

    private static long lcgNext(long l, long val) {
        l *= l * 6364136223846793005L + 1442695040888963407L;
        return l + val;
    }

    @FunctionalInterface
    public interface BiomeResolver {
        int getNoiseBiome(int quartX, int quartY, int quartZ);
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.chunk;

import net.minecraft.core.IdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.Configuration;
import net.minecraft.world.level.chunk.GlobalPalette;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.chunk.LinearPalette;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.SingleValuePalette;

public abstract class Strategy<T> {
    private static final Palette.Factory SINGLE_VALUE_PALETTE_FACTORY = SingleValuePalette::create;
    private static final Palette.Factory LINEAR_PALETTE_FACTORY = LinearPalette::create;
    private static final Palette.Factory HASHMAP_PALETTE_FACTORY = HashMapPalette::create;
    static final Configuration ZERO_BITS = new Configuration.Simple(SINGLE_VALUE_PALETTE_FACTORY, 0);
    static final Configuration ONE_BIT_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 1);
    static final Configuration TWO_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 2);
    static final Configuration THREE_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 3);
    static final Configuration FOUR_BITS_LINEAR = new Configuration.Simple(LINEAR_PALETTE_FACTORY, 4);
    static final Configuration FIVE_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 5);
    static final Configuration SIX_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 6);
    static final Configuration SEVEN_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 7);
    static final Configuration EIGHT_BITS_HASHMAP = new Configuration.Simple(HASHMAP_PALETTE_FACTORY, 8);
    private final IdMap<T> globalMap;
    private final GlobalPalette<T> globalPalette;
    protected final int globalPaletteBitsInMemory;
    private final int bitsPerAxis;
    private final int entryCount;

    Strategy(IdMap<T> idMap, int n) {
        this.globalMap = idMap;
        this.globalPalette = new GlobalPalette<T>(idMap);
        this.globalPaletteBitsInMemory = Strategy.minimumBitsRequiredForDistinctValues(idMap.size());
        this.bitsPerAxis = n;
        this.entryCount = 1 << n * 3;
    }

    public static <T> Strategy<T> createForBlockStates(IdMap<T> idMap) {
        return new Strategy<T>((IdMap)idMap, 4){

            @Override
            public Configuration getConfigurationForBitCount(int n) {
                return switch (n) {
                    case 0 -> ZERO_BITS;
                    case 1, 2, 3, 4 -> FOUR_BITS_LINEAR;
                    case 5 -> FIVE_BITS_HASHMAP;
                    case 6 -> SIX_BITS_HASHMAP;
                    case 7 -> SEVEN_BITS_HASHMAP;
                    case 8 -> EIGHT_BITS_HASHMAP;
                    default -> new Configuration.Global(this.globalPaletteBitsInMemory, n);
                };
            }
        };
    }

    public static <T> Strategy<T> createForBiomes(IdMap<T> idMap) {
        return new Strategy<T>((IdMap)idMap, 2){

            @Override
            public Configuration getConfigurationForBitCount(int n) {
                return switch (n) {
                    case 0 -> ZERO_BITS;
                    case 1 -> ONE_BIT_LINEAR;
                    case 2 -> TWO_BITS_LINEAR;
                    case 3 -> THREE_BITS_LINEAR;
                    default -> new Configuration.Global(this.globalPaletteBitsInMemory, n);
                };
            }
        };
    }

    public int entryCount() {
        return this.entryCount;
    }

    public int getIndex(int n, int n2, int n3) {
        return (n2 << this.bitsPerAxis | n3) << this.bitsPerAxis | n;
    }

    public IdMap<T> globalMap() {
        return this.globalMap;
    }

    public GlobalPalette<T> globalPalette() {
        return this.globalPalette;
    }

    protected abstract Configuration getConfigurationForBitCount(int var1);

    protected Configuration getConfigurationForPaletteSize(int n) {
        int n2 = Strategy.minimumBitsRequiredForDistinctValues(n);
        return this.getConfigurationForBitCount(n2);
    }

    private static int minimumBitsRequiredForDistinctValues(int n) {
        return Mth.ceillog2(n);
    }
}


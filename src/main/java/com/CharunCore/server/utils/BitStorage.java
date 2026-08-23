package com.CharunCore.server.utils;

public class BitStorage {
    /**
     * Packs an array of integer values into longs using Minecraft's chunk-data format.
     *
     * CRITICAL: Minecraft protocol 774 (1.21.4+) uses LSB-first packing.
     *   value[0] goes into bits [0, bits)  of long[0]
     *   value[1] goes into bits [bits, 2*bits) of long[0]
     *   ...
     *   value[valuesPerLong-1] goes into bits [(valuesPerLong-1)*bits, valuesPerLong*bits) of long[0]
     *
     * The previous implementation reversed the write order (MSB-first within each long),
     * which caused the client to misinterpret all block data → crash on chunk load.
     */
    public static long[] pack(int bits, int[] data) {
        int valuesPerLong = 64 / bits;
        int longCount = (data.length + valuesPerLong - 1) / valuesPerLong;
        long[] res = new long[longCount];
        long mask = (1L << bits) - 1;

        for (int i = 0; i < data.length; i++) {
            int longIndex = i / valuesPerLong;
            int bitOffset = (i % valuesPerLong) * bits;
            res[longIndex] |= ((long) data[i] & mask) << bitOffset;
        }
        return res;
    }

    /**
     * Unpacks an array of integer values from a long array.
     * Inverse of pack() — used for reading Anvil chunk data.
     */
    public static int[] unpack(int bits, long[] data, int count) {
        int valuesPerLong = 64 / bits;
        long mask = (1L << bits) - 1;
        int[] res = new int[count];

        for (int i = 0; i < count; i++) {
            int longIndex = i / valuesPerLong;
            if (longIndex >= data.length) break;
            int bitOffset = (i % valuesPerLong) * bits;
            res[i] = (int) ((data[longIndex] >> bitOffset) & mask);
        }
        return res;
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LpVec3 {
    private static final int DATA_BITS = 15;
    private static final int DATA_BITS_MASK = Short.MAX_VALUE;
    private static final double MAX_QUANTIZED_VALUE = 32766.0;
    private static final int SCALE_BITS = 2;
    private static final int SCALE_BITS_MASK = 3;
    private static final int CONTINUATION_FLAG = 4;
    private static final int X_OFFSET = 3;
    private static final int Y_OFFSET = 18;
    private static final int Z_OFFSET = 33;
    public static final double ABS_MAX_VALUE = 1.7179869183E10;
    public static final double ABS_MIN_VALUE = 3.051944088384301E-5;

    public static boolean hasContinuationBit(int n) {
        return (n & 4) == 4;
    }

    public static Vec3 read(ByteBuf byteBuf) {
        short s = byteBuf.readUnsignedByte();
        if (s == 0) {
            return Vec3.ZERO;
        }
        short s2 = byteBuf.readUnsignedByte();
        long l = byteBuf.readUnsignedInt();
        long l2 = l << 16 | (long)(s2 << 8) | (long)s;
        long l3 = s & 3;
        if (LpVec3.hasContinuationBit(s)) {
            l3 |= ((long)VarInt.read(byteBuf) & 0xFFFFFFFFL) << 2;
        }
        return new Vec3(LpVec3.unpack(l2 >> 3) * (double)l3, LpVec3.unpack(l2 >> 18) * (double)l3, LpVec3.unpack(l2 >> 33) * (double)l3);
    }

    public static void write(ByteBuf byteBuf, Vec3 vec3) {
        double d;
        double d2;
        double d3 = LpVec3.sanitize(vec3.x);
        double d4 = Mth.absMax(d3, Mth.absMax(d2 = LpVec3.sanitize(vec3.y), d = LpVec3.sanitize(vec3.z)));
        if (d4 < 3.051944088384301E-5) {
            byteBuf.writeByte(0);
            return;
        }
        long l = Mth.ceilLong(d4);
        boolean bl = (l & 3L) != l;
        long l2 = bl ? l & 3L | 4L : l;
        long l3 = LpVec3.pack(d3 / (double)l) << 3;
        long l4 = LpVec3.pack(d2 / (double)l) << 18;
        long l5 = LpVec3.pack(d / (double)l) << 33;
        long l6 = l2 | l3 | l4 | l5;
        byteBuf.writeByte((int)((byte)l6));
        byteBuf.writeByte((int)((byte)(l6 >> 8)));
        byteBuf.writeInt((int)(l6 >> 16));
        if (bl) {
            VarInt.write(byteBuf, (int)(l >> 2));
        }
    }

    private static double sanitize(double d) {
        return Double.isNaN(d) ? 0.0 : Math.clamp(d, -1.7179869183E10, 1.7179869183E10);
    }

    private static long pack(double d) {
        return Math.round((d * 0.5 + 0.5) * 32766.0);
    }

    private static double unpack(long l) {
        return Math.min((double)(l & 0x7FFFL), 32766.0) * 2.0 / 32766.0 - 1.0;
    }
}


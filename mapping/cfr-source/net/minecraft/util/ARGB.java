/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3f
 *  org.joml.Vector4f
 */
package net.minecraft.util;

import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ARGB {
    private static final int LINEAR_CHANNEL_DEPTH = 1024;
    private static final short[] SRGB_TO_LINEAR = Util.make(new short[256], sArray -> {
        for (int i = 0; i < ((short[])sArray).length; ++i) {
            float f = (float)i / 255.0f;
            sArray[i] = (short)Math.round(ARGB.computeSrgbToLinear(f) * 1023.0f);
        }
    });
    private static final byte[] LINEAR_TO_SRGB = Util.make(new byte[1024], byArray -> {
        for (int i = 0; i < ((byte[])byArray).length; ++i) {
            float f = (float)i / 1023.0f;
            byArray[i] = (byte)Math.round(ARGB.computeLinearToSrgb(f) * 255.0f);
        }
    });

    private static float computeSrgbToLinear(float f) {
        if (f >= 0.04045f) {
            return (float)Math.pow(((double)f + 0.055) / 1.055, 2.4);
        }
        return f / 12.92f;
    }

    private static float computeLinearToSrgb(float f) {
        if (f >= 0.0031308f) {
            return (float)(1.055 * Math.pow(f, 0.4166666666666667) - 0.055);
        }
        return 12.92f * f;
    }

    public static float srgbToLinearChannel(int n) {
        return (float)SRGB_TO_LINEAR[n] / 1023.0f;
    }

    public static int linearToSrgbChannel(float f) {
        return LINEAR_TO_SRGB[Mth.floor(f * 1023.0f)] & 0xFF;
    }

    public static int meanLinear(int n, int n2, int n3, int n4) {
        return ARGB.color((ARGB.alpha(n) + ARGB.alpha(n2) + ARGB.alpha(n3) + ARGB.alpha(n4)) / 4, ARGB.linearChannelMean(ARGB.red(n), ARGB.red(n2), ARGB.red(n3), ARGB.red(n4)), ARGB.linearChannelMean(ARGB.green(n), ARGB.green(n2), ARGB.green(n3), ARGB.green(n4)), ARGB.linearChannelMean(ARGB.blue(n), ARGB.blue(n2), ARGB.blue(n3), ARGB.blue(n4)));
    }

    private static int linearChannelMean(int n, int n2, int n3, int n4) {
        int n5 = (SRGB_TO_LINEAR[n] + SRGB_TO_LINEAR[n2] + SRGB_TO_LINEAR[n3] + SRGB_TO_LINEAR[n4]) / 4;
        return LINEAR_TO_SRGB[n5] & 0xFF;
    }

    public static int alpha(int n) {
        return n >>> 24;
    }

    public static int red(int n) {
        return n >> 16 & 0xFF;
    }

    public static int green(int n) {
        return n >> 8 & 0xFF;
    }

    public static int blue(int n) {
        return n & 0xFF;
    }

    public static int color(int n, int n2, int n3, int n4) {
        return (n & 0xFF) << 24 | (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
    }

    public static int color(int n, int n2, int n3) {
        return ARGB.color(255, n, n2, n3);
    }

    public static int color(Vec3 vec3) {
        return ARGB.color(ARGB.as8BitChannel((float)vec3.x()), ARGB.as8BitChannel((float)vec3.y()), ARGB.as8BitChannel((float)vec3.z()));
    }

    public static int multiply(int n, int n2) {
        if (n == -1) {
            return n2;
        }
        if (n2 == -1) {
            return n;
        }
        return ARGB.color(ARGB.alpha(n) * ARGB.alpha(n2) / 255, ARGB.red(n) * ARGB.red(n2) / 255, ARGB.green(n) * ARGB.green(n2) / 255, ARGB.blue(n) * ARGB.blue(n2) / 255);
    }

    public static int addRgb(int n, int n2) {
        return ARGB.color(ARGB.alpha(n), Math.min(ARGB.red(n) + ARGB.red(n2), 255), Math.min(ARGB.green(n) + ARGB.green(n2), 255), Math.min(ARGB.blue(n) + ARGB.blue(n2), 255));
    }

    public static int subtractRgb(int n, int n2) {
        return ARGB.color(ARGB.alpha(n), Math.max(ARGB.red(n) - ARGB.red(n2), 0), Math.max(ARGB.green(n) - ARGB.green(n2), 0), Math.max(ARGB.blue(n) - ARGB.blue(n2), 0));
    }

    public static int multiplyAlpha(int n, float f) {
        if (n == 0 || f <= 0.0f) {
            return 0;
        }
        if (f >= 1.0f) {
            return n;
        }
        return ARGB.color(ARGB.alphaFloat(n) * f, n);
    }

    public static int scaleRGB(int n, float f) {
        return ARGB.scaleRGB(n, f, f, f);
    }

    public static int scaleRGB(int n, float f, float f2, float f3) {
        return ARGB.color(ARGB.alpha(n), Math.clamp((long)((int)((float)ARGB.red(n) * f)), 0, 255), Math.clamp((long)((int)((float)ARGB.green(n) * f2)), 0, 255), Math.clamp((long)((int)((float)ARGB.blue(n) * f3)), 0, 255));
    }

    public static int scaleRGB(int n, int n2) {
        return ARGB.color(ARGB.alpha(n), Math.clamp((long)ARGB.red(n) * (long)n2 / 255L, 0, 255), Math.clamp((long)ARGB.green(n) * (long)n2 / 255L, 0, 255), Math.clamp((long)ARGB.blue(n) * (long)n2 / 255L, 0, 255));
    }

    public static int greyscale(int n) {
        int n2 = (int)((float)ARGB.red(n) * 0.3f + (float)ARGB.green(n) * 0.59f + (float)ARGB.blue(n) * 0.11f);
        return ARGB.color(ARGB.alpha(n), n2, n2, n2);
    }

    public static int alphaBlend(int n, int n2) {
        int n3 = ARGB.alpha(n);
        int n4 = ARGB.alpha(n2);
        if (n4 == 255) {
            return n2;
        }
        if (n4 == 0) {
            return n;
        }
        int n5 = n4 + n3 * (255 - n4) / 255;
        return ARGB.color(n5, ARGB.alphaBlendChannel(n5, n4, ARGB.red(n), ARGB.red(n2)), ARGB.alphaBlendChannel(n5, n4, ARGB.green(n), ARGB.green(n2)), ARGB.alphaBlendChannel(n5, n4, ARGB.blue(n), ARGB.blue(n2)));
    }

    private static int alphaBlendChannel(int n, int n2, int n3, int n4) {
        return (n4 * n2 + n3 * (n - n2)) / n;
    }

    public static int srgbLerp(float f, int n, int n2) {
        int n3 = Mth.lerpInt(f, ARGB.alpha(n), ARGB.alpha(n2));
        int n4 = Mth.lerpInt(f, ARGB.red(n), ARGB.red(n2));
        int n5 = Mth.lerpInt(f, ARGB.green(n), ARGB.green(n2));
        int n6 = Mth.lerpInt(f, ARGB.blue(n), ARGB.blue(n2));
        return ARGB.color(n3, n4, n5, n6);
    }

    public static int linearLerp(float f, int n, int n2) {
        return ARGB.color(Mth.lerpInt(f, ARGB.alpha(n), ARGB.alpha(n2)), LINEAR_TO_SRGB[Mth.lerpInt(f, SRGB_TO_LINEAR[ARGB.red(n)], SRGB_TO_LINEAR[ARGB.red(n2)])] & 0xFF, LINEAR_TO_SRGB[Mth.lerpInt(f, SRGB_TO_LINEAR[ARGB.green(n)], SRGB_TO_LINEAR[ARGB.green(n2)])] & 0xFF, LINEAR_TO_SRGB[Mth.lerpInt(f, SRGB_TO_LINEAR[ARGB.blue(n)], SRGB_TO_LINEAR[ARGB.blue(n2)])] & 0xFF);
    }

    public static int opaque(int n) {
        return n | 0xFF000000;
    }

    public static int transparent(int n) {
        return n & 0xFFFFFF;
    }

    public static int color(int n, int n2) {
        return n << 24 | n2 & 0xFFFFFF;
    }

    public static int color(float f, int n) {
        return ARGB.as8BitChannel(f) << 24 | n & 0xFFFFFF;
    }

    public static int white(float f) {
        return ARGB.as8BitChannel(f) << 24 | 0xFFFFFF;
    }

    public static int white(int n) {
        return n << 24 | 0xFFFFFF;
    }

    public static int black(float f) {
        return ARGB.as8BitChannel(f) << 24;
    }

    public static int black(int n) {
        return n << 24;
    }

    public static int colorFromFloat(float f, float f2, float f3, float f4) {
        return ARGB.color(ARGB.as8BitChannel(f), ARGB.as8BitChannel(f2), ARGB.as8BitChannel(f3), ARGB.as8BitChannel(f4));
    }

    public static Vector3f vector3fFromRGB24(int n) {
        return new Vector3f(ARGB.redFloat(n), ARGB.greenFloat(n), ARGB.blueFloat(n));
    }

    public static Vector4f vector4fFromARGB32(int n) {
        return new Vector4f(ARGB.redFloat(n), ARGB.greenFloat(n), ARGB.blueFloat(n), ARGB.alphaFloat(n));
    }

    public static int average(int n, int n2) {
        return ARGB.color((ARGB.alpha(n) + ARGB.alpha(n2)) / 2, (ARGB.red(n) + ARGB.red(n2)) / 2, (ARGB.green(n) + ARGB.green(n2)) / 2, (ARGB.blue(n) + ARGB.blue(n2)) / 2);
    }

    public static int as8BitChannel(float f) {
        return Mth.floor(f * 255.0f);
    }

    public static float alphaFloat(int n) {
        return ARGB.from8BitChannel(ARGB.alpha(n));
    }

    public static float redFloat(int n) {
        return ARGB.from8BitChannel(ARGB.red(n));
    }

    public static float greenFloat(int n) {
        return ARGB.from8BitChannel(ARGB.green(n));
    }

    public static float blueFloat(int n) {
        return ARGB.from8BitChannel(ARGB.blue(n));
    }

    private static float from8BitChannel(int n) {
        return (float)n / 255.0f;
    }

    public static int toABGR(int n) {
        return n & 0xFF00FF00 | (n & 0xFF0000) >> 16 | (n & 0xFF) << 16;
    }

    public static int fromABGR(int n) {
        return ARGB.toABGR(n);
    }

    public static int setBrightness(int n, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        int n2 = ARGB.red(n);
        int n3 = ARGB.green(n);
        int n4 = ARGB.blue(n);
        int n5 = ARGB.alpha(n);
        int n6 = Math.max(Math.max(n2, n3), n4);
        int n7 = Math.min(Math.min(n2, n3), n4);
        float f6 = n6 - n7;
        float f7 = n6 != 0 ? f6 / (float)n6 : 0.0f;
        if (f7 == 0.0f) {
            f5 = 0.0f;
        } else {
            f4 = (float)(n6 - n2) / f6;
            f3 = (float)(n6 - n3) / f6;
            f2 = (float)(n6 - n4) / f6;
            f5 = n2 == n6 ? f2 - f3 : (n3 == n6 ? 2.0f + f4 - f2 : 4.0f + f3 - f4);
            if ((f5 /= 6.0f) < 0.0f) {
                f5 += 1.0f;
            }
        }
        if (f7 == 0.0f) {
            n3 = n4 = Math.round(f * 255.0f);
            n2 = n4;
            return ARGB.color(n5, n2, n3, n4);
        }
        f4 = (f5 - (float)Math.floor(f5)) * 6.0f;
        f3 = f4 - (float)Math.floor(f4);
        f2 = f * (1.0f - f7);
        float f8 = f * (1.0f - f7 * f3);
        float f9 = f * (1.0f - f7 * (1.0f - f3));
        switch ((int)f4) {
            case 0: {
                n2 = Math.round(f * 255.0f);
                n3 = Math.round(f9 * 255.0f);
                n4 = Math.round(f2 * 255.0f);
                break;
            }
            case 1: {
                n2 = Math.round(f8 * 255.0f);
                n3 = Math.round(f * 255.0f);
                n4 = Math.round(f2 * 255.0f);
                break;
            }
            case 2: {
                n2 = Math.round(f2 * 255.0f);
                n3 = Math.round(f * 255.0f);
                n4 = Math.round(f9 * 255.0f);
                break;
            }
            case 3: {
                n2 = Math.round(f2 * 255.0f);
                n3 = Math.round(f8 * 255.0f);
                n4 = Math.round(f * 255.0f);
                break;
            }
            case 4: {
                n2 = Math.round(f9 * 255.0f);
                n3 = Math.round(f2 * 255.0f);
                n4 = Math.round(f * 255.0f);
                break;
            }
            case 5: {
                n2 = Math.round(f * 255.0f);
                n3 = Math.round(f2 * 255.0f);
                n4 = Math.round(f8 * 255.0f);
            }
        }
        return ARGB.color(n5, n2, n3, n4);
    }
}


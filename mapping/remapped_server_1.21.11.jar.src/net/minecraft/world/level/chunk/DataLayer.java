/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataLayer
/*     */ {
/*     */   public static final int LAYER_COUNT = 16;
/*     */   public static final int LAYER_SIZE = 128;
/*     */   public static final int SIZE = 2048;
/*     */   private static final int NIBBLE_SIZE = 4;
/*     */   protected byte[] data;
/*     */   private int defaultValue;
/*     */   
/*     */   public DataLayer() {
/*  21 */     this(0);
/*     */   }
/*     */   
/*     */   public DataLayer(int paramInt) {
/*  25 */     this.defaultValue = paramInt;
/*     */   }
/*     */   
/*     */   public DataLayer(byte[] paramArrayOfbyte) {
/*  29 */     this.data = paramArrayOfbyte;
/*  30 */     this.defaultValue = 0;
/*     */     
/*  32 */     if (paramArrayOfbyte.length != 2048) {
/*  33 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("DataLayer should be 2048 bytes not: " + paramArrayOfbyte.length));
/*     */     }
/*     */   }
/*     */   
/*     */   public int get(int paramInt1, int paramInt2, int paramInt3) {
/*  38 */     return get(getIndex(paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */   
/*     */   public void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  42 */     set(getIndex(paramInt1, paramInt2, paramInt3), paramInt4);
/*     */   }
/*     */   
/*     */   private static int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/*  46 */     return paramInt2 << 8 | paramInt3 << 4 | paramInt1;
/*     */   }
/*     */   
/*     */   private int get(int paramInt) {
/*  50 */     if (this.data == null) {
/*  51 */       return this.defaultValue;
/*     */     }
/*  53 */     int i = getByteIndex(paramInt);
/*  54 */     int j = getNibbleIndex(paramInt);
/*  55 */     return this.data[i] >> 4 * j & 0xF;
/*     */   }
/*     */   
/*     */   private void set(int paramInt1, int paramInt2) {
/*  59 */     byte[] arrayOfByte = getData();
/*  60 */     int i = getByteIndex(paramInt1);
/*  61 */     int j = getNibbleIndex(paramInt1);
/*     */     
/*  63 */     int k = 15 << 4 * j ^ 0xFFFFFFFF;
/*  64 */     int m = (paramInt2 & 0xF) << 4 * j;
/*  65 */     arrayOfByte[i] = (byte)(arrayOfByte[i] & k | m);
/*     */   }
/*     */   
/*     */   private static int getNibbleIndex(int paramInt) {
/*  69 */     return paramInt & 0x1;
/*     */   }
/*     */   
/*     */   private static int getByteIndex(int paramInt) {
/*  73 */     return paramInt >> 1;
/*     */   }
/*     */   
/*     */   public void fill(int paramInt) {
/*  77 */     this.defaultValue = paramInt;
/*  78 */     this.data = null;
/*     */   }
/*     */   
/*     */   private static byte packFilled(int paramInt) {
/*  82 */     byte b = (byte)paramInt;
/*  83 */     for (byte b1 = 4; b1 < 8; b1 += 4) {
/*  84 */       b = (byte)(b | paramInt << b1);
/*     */     }
/*  86 */     return b;
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/*  90 */     if (this.data == null) {
/*  91 */       this.data = new byte[2048];
/*  92 */       if (this.defaultValue != 0) {
/*  93 */         Arrays.fill(this.data, packFilled(this.defaultValue));
/*     */       }
/*     */     } 
/*  96 */     return this.data;
/*     */   }
/*     */   
/*     */   public DataLayer copy() {
/* 100 */     if (this.data == null) {
/* 101 */       return new DataLayer(this.defaultValue);
/*     */     }
/* 103 */     return new DataLayer((byte[])this.data.clone());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     StringBuilder stringBuilder = new StringBuilder();
/* 109 */     for (byte b = 0; b < 'က'; b++) {
/* 110 */       stringBuilder.append(Integer.toHexString(get(b)));
/* 111 */       if ((b & 0xF) == 15) {
/* 112 */         stringBuilder.append("\n");
/*     */       }
/* 114 */       if ((b & 0xFF) == 255) {
/* 115 */         stringBuilder.append("\n");
/*     */       }
/*     */     } 
/* 118 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public String layerToString(int paramInt) {
/* 124 */     StringBuilder stringBuilder = new StringBuilder();
/* 125 */     for (byte b = 0; b < 'Ā'; b++) {
/* 126 */       stringBuilder.append(Integer.toHexString(get(b)));
/* 127 */       if ((b & 0xF) == 15) {
/* 128 */         stringBuilder.append("\n");
/*     */       }
/*     */     } 
/* 131 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public boolean isDefinitelyHomogenous() {
/* 135 */     return (this.data == null);
/*     */   }
/*     */   
/*     */   public boolean isDefinitelyFilledWith(int paramInt) {
/* 139 */     return (this.data == null && this.defaultValue == paramInt);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 143 */     return (this.data == null && this.defaultValue == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\DataLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
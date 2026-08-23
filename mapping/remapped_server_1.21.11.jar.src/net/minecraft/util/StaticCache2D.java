/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StaticCache2D<T>
/*    */ {
/*    */   private final int minX;
/*    */   private final int minZ;
/*    */   private final int sizeX;
/*    */   private final int sizeZ;
/*    */   private final Object[] cache;
/*    */   
/*    */   public static <T> StaticCache2D<T> create(int paramInt1, int paramInt2, int paramInt3, Initializer<T> paramInitializer) {
/* 19 */     int i = paramInt1 - paramInt3;
/* 20 */     int j = paramInt2 - paramInt3;
/* 21 */     int k = 2 * paramInt3 + 1;
/* 22 */     return new StaticCache2D<>(i, j, k, k, paramInitializer);
/*    */   }
/*    */   
/*    */   private StaticCache2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Initializer<T> paramInitializer) {
/* 26 */     this.minX = paramInt1;
/* 27 */     this.minZ = paramInt2;
/* 28 */     this.sizeX = paramInt3;
/* 29 */     this.sizeZ = paramInt4;
/* 30 */     this.cache = new Object[this.sizeX * this.sizeZ];
/* 31 */     for (int i = paramInt1; i < paramInt1 + paramInt3; i++) {
/* 32 */       for (int j = paramInt2; j < paramInt2 + paramInt4; j++) {
/* 33 */         this.cache[getIndex(i, j)] = paramInitializer.get(i, j);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<T> paramConsumer) {
/* 40 */     for (Object object : this.cache) {
/* 41 */       paramConsumer.accept((T)object);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(int paramInt1, int paramInt2) {
/* 47 */     if (!contains(paramInt1, paramInt2)) {
/* 48 */       throw new IllegalArgumentException("Requested out of range value (" + paramInt1 + "," + paramInt2 + ") from " + String.valueOf(this));
/*    */     }
/* 50 */     return (T)this.cache[getIndex(paramInt1, paramInt2)];
/*    */   }
/*    */   
/*    */   public boolean contains(int paramInt1, int paramInt2) {
/* 54 */     int i = paramInt1 - this.minX;
/* 55 */     int j = paramInt2 - this.minZ;
/* 56 */     return (i >= 0 && i < this.sizeX && j >= 0 && j < this.sizeZ);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return String.format(Locale.ROOT, "StaticCache2D[%d, %d, %d, %d]", new Object[] { Integer.valueOf(this.minX), Integer.valueOf(this.minZ), Integer.valueOf(this.minX + this.sizeX), Integer.valueOf(this.minZ + this.sizeZ) });
/*    */   }
/*    */   
/*    */   private int getIndex(int paramInt1, int paramInt2) {
/* 66 */     int i = paramInt1 - this.minX;
/* 67 */     int j = paramInt2 - this.minZ;
/* 68 */     return i * this.sizeZ + j;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Initializer<T> {
/*    */     T get(int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\StaticCache2D.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
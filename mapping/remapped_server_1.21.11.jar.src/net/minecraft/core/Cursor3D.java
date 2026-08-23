/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Cursor3D
/*    */ {
/*    */   public static final int TYPE_INSIDE = 0;
/*    */   public static final int TYPE_FACE = 1;
/*    */   public static final int TYPE_EDGE = 2;
/*    */   public static final int TYPE_CORNER = 3;
/*    */   private final int originX;
/*    */   private final int originY;
/*    */   private final int originZ;
/*    */   private final int width;
/*    */   private final int height;
/*    */   private final int depth;
/*    */   private final int end;
/*    */   private int index;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public Cursor3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 24 */     this.originX = paramInt1;
/* 25 */     this.originY = paramInt2;
/* 26 */     this.originZ = paramInt3;
/*    */     
/* 28 */     this.width = paramInt4 - paramInt1 + 1;
/* 29 */     this.height = paramInt5 - paramInt2 + 1;
/* 30 */     this.depth = paramInt6 - paramInt3 + 1;
/* 31 */     this.end = this.width * this.height * this.depth;
/*    */   }
/*    */   
/*    */   public boolean advance() {
/* 35 */     if (this.index == this.end) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     this.x = this.index % this.width;
/* 40 */     int i = this.index / this.width;
/* 41 */     this.y = i % this.height;
/* 42 */     this.z = i / this.height;
/*    */     
/* 44 */     this.index++;
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public int nextX() {
/* 49 */     return this.originX + this.x;
/*    */   }
/*    */   
/*    */   public int nextY() {
/* 53 */     return this.originY + this.y;
/*    */   }
/*    */   
/*    */   public int nextZ() {
/* 57 */     return this.originZ + this.z;
/*    */   }
/*    */   
/*    */   public int getNextType() {
/* 61 */     byte b = 0;
/* 62 */     if (this.x == 0 || this.x == this.width - 1) {
/* 63 */       b++;
/*    */     }
/* 65 */     if (this.y == 0 || this.y == this.height - 1) {
/* 66 */       b++;
/*    */     }
/* 68 */     if (this.z == 0 || this.z == this.depth - 1) {
/* 69 */       b++;
/*    */     }
/* 71 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\Cursor3D.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
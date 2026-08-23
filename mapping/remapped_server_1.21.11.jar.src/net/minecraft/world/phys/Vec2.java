/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Vec2
/*    */ {
/* 11 */   public static final Vec2 ZERO = new Vec2(0.0F, 0.0F);
/* 12 */   public static final Vec2 ONE = new Vec2(1.0F, 1.0F);
/* 13 */   public static final Vec2 UNIT_X = new Vec2(1.0F, 0.0F);
/* 14 */   public static final Vec2 NEG_UNIT_X = new Vec2(-1.0F, 0.0F);
/* 15 */   public static final Vec2 UNIT_Y = new Vec2(0.0F, 1.0F);
/* 16 */   public static final Vec2 NEG_UNIT_Y = new Vec2(0.0F, -1.0F);
/* 17 */   public static final Vec2 MAX = new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);
/* 18 */   public static final Vec2 MIN = new Vec2(Float.MIN_VALUE, Float.MIN_VALUE);
/*    */   static {
/* 20 */     CODEC = Codec.FLOAT.listOf().comapFlatMap(paramList -> Util.fixedSize(paramList, 2).map(()), paramVec2 -> List.of(Float.valueOf(paramVec2.x), Float.valueOf(paramVec2.y)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<Vec2> CODEC;
/*    */   public final float x;
/*    */   public final float y;
/*    */   
/*    */   public Vec2(float paramFloat1, float paramFloat2) {
/* 29 */     this.x = paramFloat1;
/* 30 */     this.y = paramFloat2;
/*    */   }
/*    */   
/*    */   public Vec2 scale(float paramFloat) {
/* 34 */     return new Vec2(this.x * paramFloat, this.y * paramFloat);
/*    */   }
/*    */   
/*    */   public float dot(Vec2 paramVec2) {
/* 38 */     return this.x * paramVec2.x + this.y * paramVec2.y;
/*    */   }
/*    */   
/*    */   public Vec2 add(Vec2 paramVec2) {
/* 42 */     return new Vec2(this.x + paramVec2.x, this.y + paramVec2.y);
/*    */   }
/*    */   
/*    */   public Vec2 add(float paramFloat) {
/* 46 */     return new Vec2(this.x + paramFloat, this.y + paramFloat);
/*    */   }
/*    */   
/*    */   public boolean equals(Vec2 paramVec2) {
/* 50 */     return (this.x == paramVec2.x && this.y == paramVec2.y);
/*    */   }
/*    */   
/*    */   public Vec2 normalized() {
/* 54 */     float f = Mth.sqrt(this.x * this.x + this.y * this.y);
/* 55 */     return (f < 1.0E-4F) ? ZERO : new Vec2(this.x / f, this.y / f);
/*    */   }
/*    */   
/*    */   public float length() {
/* 59 */     return Mth.sqrt(this.x * this.x + this.y * this.y);
/*    */   }
/*    */   
/*    */   public float lengthSquared() {
/* 63 */     return this.x * this.x + this.y * this.y;
/*    */   }
/*    */   
/*    */   public float distanceToSqr(Vec2 paramVec2) {
/* 67 */     float f1 = paramVec2.x - this.x;
/* 68 */     float f2 = paramVec2.y - this.y;
/* 69 */     return f1 * f1 + f2 * f2;
/*    */   }
/*    */   
/*    */   public Vec2 negated() {
/* 73 */     return new Vec2(-this.x, -this.y);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\Vec2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.food;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/*    */   private int nutrition;
/*    */   private float saturationModifier;
/*    */   private boolean canAlwaysEat;
/*    */   
/*    */   public Builder nutrition(int paramInt) {
/* 56 */     this.nutrition = paramInt;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder saturationModifier(float paramFloat) {
/* 61 */     this.saturationModifier = paramFloat;
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public Builder alwaysEdible() {
/* 66 */     this.canAlwaysEat = true;
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public FoodProperties build() {
/* 71 */     float f = FoodConstants.saturationByModifier(this.nutrition, this.saturationModifier);
/* 72 */     return new FoodProperties(this.nutrition, f, this.canAlwaysEat);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\food\FoodProperties$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
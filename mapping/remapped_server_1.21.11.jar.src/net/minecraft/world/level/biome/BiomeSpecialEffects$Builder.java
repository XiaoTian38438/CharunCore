/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
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
/* 27 */   private OptionalInt waterColor = OptionalInt.empty();
/* 28 */   private Optional<Integer> foliageColorOverride = Optional.empty();
/* 29 */   private Optional<Integer> dryFoliageColorOverride = Optional.empty();
/* 30 */   private Optional<Integer> grassColorOverride = Optional.empty();
/* 31 */   private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/*    */   
/*    */   public Builder waterColor(int paramInt) {
/* 34 */     this.waterColor = OptionalInt.of(paramInt);
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public Builder foliageColorOverride(int paramInt) {
/* 39 */     this.foliageColorOverride = Optional.of(Integer.valueOf(paramInt));
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public Builder dryFoliageColorOverride(int paramInt) {
/* 44 */     this.dryFoliageColorOverride = Optional.of(Integer.valueOf(paramInt));
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public Builder grassColorOverride(int paramInt) {
/* 49 */     this.grassColorOverride = Optional.of(Integer.valueOf(paramInt));
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier paramGrassColorModifier) {
/* 54 */     this.grassColorModifier = paramGrassColorModifier;
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public BiomeSpecialEffects build() {
/* 59 */     return new BiomeSpecialEffects(this.waterColor
/* 60 */         .orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColorOverride, this.dryFoliageColorOverride, this.grassColorOverride, this.grassColorModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
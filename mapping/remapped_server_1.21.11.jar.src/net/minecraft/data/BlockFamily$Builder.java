/*     */ package net.minecraft.data;
/*     */ 
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final BlockFamily family;
/*     */   
/*     */   public Builder(Block paramBlock) {
/*  93 */     this.family = new BlockFamily(paramBlock);
/*     */   }
/*     */   
/*     */   public BlockFamily getFamily() {
/*  97 */     return this.family;
/*     */   }
/*     */   
/*     */   public Builder button(Block paramBlock) {
/* 101 */     this.family.variants.put(BlockFamily.Variant.BUTTON, paramBlock);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public Builder chiseled(Block paramBlock) {
/* 106 */     this.family.variants.put(BlockFamily.Variant.CHISELED, paramBlock);
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public Builder mosaic(Block paramBlock) {
/* 111 */     this.family.variants.put(BlockFamily.Variant.MOSAIC, paramBlock);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public Builder cracked(Block paramBlock) {
/* 116 */     this.family.variants.put(BlockFamily.Variant.CRACKED, paramBlock);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public Builder cut(Block paramBlock) {
/* 121 */     this.family.variants.put(BlockFamily.Variant.CUT, paramBlock);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public Builder door(Block paramBlock) {
/* 126 */     this.family.variants.put(BlockFamily.Variant.DOOR, paramBlock);
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public Builder customFence(Block paramBlock) {
/* 131 */     this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE, paramBlock);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Builder fence(Block paramBlock) {
/* 137 */     this.family.variants.put(BlockFamily.Variant.FENCE, paramBlock);
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public Builder customFenceGate(Block paramBlock) {
/* 142 */     this.family.variants.put(BlockFamily.Variant.CUSTOM_FENCE_GATE, paramBlock);
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Builder fenceGate(Block paramBlock) {
/* 148 */     this.family.variants.put(BlockFamily.Variant.FENCE_GATE, paramBlock);
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sign(Block paramBlock1, Block paramBlock2) {
/* 153 */     this.family.variants.put(BlockFamily.Variant.SIGN, paramBlock1);
/* 154 */     this.family.variants.put(BlockFamily.Variant.WALL_SIGN, paramBlock2);
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public Builder slab(Block paramBlock) {
/* 159 */     this.family.variants.put(BlockFamily.Variant.SLAB, paramBlock);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder stairs(Block paramBlock) {
/* 164 */     this.family.variants.put(BlockFamily.Variant.STAIRS, paramBlock);
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder pressurePlate(Block paramBlock) {
/* 169 */     this.family.variants.put(BlockFamily.Variant.PRESSURE_PLATE, paramBlock);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Builder polished(Block paramBlock) {
/* 174 */     this.family.variants.put(BlockFamily.Variant.POLISHED, paramBlock);
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder trapdoor(Block paramBlock) {
/* 179 */     this.family.variants.put(BlockFamily.Variant.TRAPDOOR, paramBlock);
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public Builder wall(Block paramBlock) {
/* 184 */     this.family.variants.put(BlockFamily.Variant.WALL, paramBlock);
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public Builder dontGenerateModel() {
/* 189 */     this.family.generateModel = false;
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public Builder dontGenerateRecipe() {
/* 194 */     this.family.generateRecipe = false;
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   public Builder recipeGroupPrefix(String paramString) {
/* 199 */     this.family.recipeGroupPrefix = paramString;
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public Builder recipeUnlockedBy(String paramString) {
/* 204 */     this.family.recipeUnlockedBy = paramString;
/* 205 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\BlockFamily$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
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
/*    */   extends LootItemConditionalFunction.Builder<SetBannerPatternFunction.Builder>
/*    */ {
/* 50 */   private final BannerPatternLayers.Builder patterns = new BannerPatternLayers.Builder();
/*    */   private final boolean append;
/*    */   
/*    */   Builder(boolean paramBoolean) {
/* 54 */     this.append = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 64 */     return new SetBannerPatternFunction(getConditions(), this.patterns.build(), this.append);
/*    */   }
/*    */   
/*    */   public Builder addPattern(Holder<BannerPattern> paramHolder, DyeColor paramDyeColor) {
/* 68 */     this.patterns.add(paramHolder, paramDyeColor);
/* 69 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetBannerPatternFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
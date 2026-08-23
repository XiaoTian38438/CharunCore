/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetBannerPatternFunction extends LootItemConditionalFunction {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and(paramInstance.group((App)BannerPatternLayers.CODEC.fieldOf("patterns").forGetter(()), (App)Codec.BOOL.fieldOf("append").forGetter(()))).apply((Applicative)paramInstance, SetBannerPatternFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetBannerPatternFunction> CODEC;
/*    */   private final BannerPatternLayers patterns;
/*    */   private final boolean append;
/*    */   
/*    */   SetBannerPatternFunction(List<LootItemCondition> paramList, BannerPatternLayers paramBannerPatternLayers, boolean paramBoolean) {
/* 27 */     super(paramList);
/* 28 */     this.patterns = paramBannerPatternLayers;
/* 29 */     this.append = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 34 */     if (this.append) {
/* 35 */       paramItemStack.update(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY, this.patterns, (paramBannerPatternLayers1, paramBannerPatternLayers2) -> (new BannerPatternLayers.Builder()).addAll(paramBannerPatternLayers1).addAll(paramBannerPatternLayers2).build());
/*    */     }
/*    */     else {
/*    */       
/* 39 */       paramItemStack.set(DataComponents.BANNER_PATTERNS, this.patterns);
/*    */     } 
/* 41 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetBannerPatternFunction> getType() {
/* 46 */     return LootItemFunctions.SET_BANNER_PATTERN;
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 50 */     private final BannerPatternLayers.Builder patterns = new BannerPatternLayers.Builder();
/*    */     private final boolean append;
/*    */     
/*    */     Builder(boolean param1Boolean) {
/* 54 */       this.append = param1Boolean;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 59 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 64 */       return new SetBannerPatternFunction(getConditions(), this.patterns.build(), this.append);
/*    */     }
/*    */     
/*    */     public Builder addPattern(Holder<BannerPattern> param1Holder, DyeColor param1DyeColor) {
/* 68 */       this.patterns.add(param1Holder, param1DyeColor);
/* 69 */       return this;
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setBannerPattern(boolean paramBoolean) {
/* 74 */     return new Builder(paramBoolean);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetBannerPatternFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
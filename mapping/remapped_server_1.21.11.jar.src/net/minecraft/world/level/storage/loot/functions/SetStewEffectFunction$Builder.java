/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
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
/*    */   extends LootItemConditionalFunction.Builder<SetStewEffectFunction.Builder>
/*    */ {
/* 80 */   private final ImmutableList.Builder<SetStewEffectFunction.EffectEntry> effects = ImmutableList.builder();
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 84 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEffect(Holder<MobEffect> paramHolder, NumberProvider paramNumberProvider) {
/* 88 */     this.effects.add(new SetStewEffectFunction.EffectEntry(paramHolder, paramNumberProvider));
/* 89 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 94 */     return new SetStewEffectFunction(getConditions(), (List<SetStewEffectFunction.EffectEntry>)this.effects.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
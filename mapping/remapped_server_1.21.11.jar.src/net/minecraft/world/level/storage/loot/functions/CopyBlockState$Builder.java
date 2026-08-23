/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
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
/*    */   extends LootItemConditionalFunction.Builder<CopyBlockState.Builder>
/*    */ {
/*    */   private final Holder<Block> block;
/* 77 */   private final ImmutableSet.Builder<Property<?>> properties = ImmutableSet.builder();
/*    */   
/*    */   Builder(Block paramBlock) {
/* 80 */     this.block = (Holder<Block>)paramBlock.builtInRegistryHolder();
/*    */   }
/*    */   
/*    */   public Builder copy(Property<?> paramProperty) {
/* 84 */     if (!((Block)this.block.value()).getStateDefinition().getProperties().contains(paramProperty)) {
/* 85 */       throw new IllegalStateException("Property " + String.valueOf(paramProperty) + " is not present on block " + String.valueOf(this.block));
/*    */     }
/* 87 */     this.properties.add(paramProperty);
/* 88 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Builder getThis() {
/* 93 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunction build() {
/* 98 */     return new CopyBlockState(getConditions(), this.block, (Set<Property<?>>)this.properties.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\CopyBlockState$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
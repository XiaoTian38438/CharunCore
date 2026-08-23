/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
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
/*    */   implements LootItemCondition.Builder
/*    */ {
/*    */   private final Holder<Block> block;
/* 52 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */   
/*    */   public Builder(Block paramBlock) {
/* 55 */     this.block = (Holder<Block>)paramBlock.builtInRegistryHolder();
/*    */   }
/*    */   
/*    */   public Builder setProperties(StatePropertiesPredicate.Builder paramBuilder) {
/* 59 */     this.properties = paramBuilder.build();
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemCondition build() {
/* 65 */     return new LootItemBlockStatePropertyCondition(this.block, this.properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemBlockStatePropertyCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
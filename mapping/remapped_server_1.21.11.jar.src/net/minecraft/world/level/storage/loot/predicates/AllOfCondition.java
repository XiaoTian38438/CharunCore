/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AllOfCondition extends CompositeLootItemCondition {
/* 10 */   public static final MapCodec<AllOfCondition> CODEC = createCodec(AllOfCondition::new);
/* 11 */   public static final Codec<AllOfCondition> INLINE_CODEC = createInlineCodec(AllOfCondition::new);
/*    */   
/*    */   AllOfCondition(List<LootItemCondition> paramList) {
/* 14 */     super(paramList, Util.allOf(paramList));
/*    */   }
/*    */   
/*    */   public static AllOfCondition allOf(List<LootItemCondition> paramList) {
/* 18 */     return new AllOfCondition(List.copyOf(paramList));
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 23 */     return LootItemConditions.ALL_OF;
/*    */   }
/*    */   
/*    */   public static class Builder extends CompositeLootItemCondition.Builder {
/*    */     public Builder(LootItemCondition.Builder... param1VarArgs) {
/* 28 */       super(param1VarArgs);
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder and(LootItemCondition.Builder param1Builder) {
/* 33 */       addTerm(param1Builder);
/* 34 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     protected LootItemCondition create(List<LootItemCondition> param1List) {
/* 39 */       return new AllOfCondition(param1List);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder allOf(LootItemCondition.Builder... paramVarArgs) {
/* 44 */     return new Builder(paramVarArgs);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\AllOfCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
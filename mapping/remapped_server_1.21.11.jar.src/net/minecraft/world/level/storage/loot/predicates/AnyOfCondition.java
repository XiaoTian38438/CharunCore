/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AnyOfCondition extends CompositeLootItemCondition {
/*  9 */   public static final MapCodec<AnyOfCondition> CODEC = createCodec(AnyOfCondition::new);
/*    */   
/*    */   AnyOfCondition(List<LootItemCondition> paramList) {
/* 12 */     super(paramList, Util.anyOf(paramList));
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 17 */     return LootItemConditions.ANY_OF;
/*    */   }
/*    */   
/*    */   public static class Builder extends CompositeLootItemCondition.Builder {
/*    */     public Builder(LootItemCondition.Builder... param1VarArgs) {
/* 22 */       super(param1VarArgs);
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder or(LootItemCondition.Builder param1Builder) {
/* 27 */       addTerm(param1Builder);
/* 28 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     protected LootItemCondition create(List<LootItemCondition> param1List) {
/* 33 */       return new AnyOfCondition(param1List);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder anyOf(LootItemCondition.Builder... paramVarArgs) {
/* 38 */     return new Builder(paramVarArgs);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\AnyOfCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
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
/*    */   implements LootItemCondition.Builder
/*    */ {
/* 69 */   private final ImmutableMap.Builder<String, IntRange> scores = ImmutableMap.builder();
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public Builder(LootContext.EntityTarget paramEntityTarget) {
/* 73 */     this.entityTarget = paramEntityTarget;
/*    */   }
/*    */   
/*    */   public Builder withScore(String paramString, IntRange paramIntRange) {
/* 77 */     this.scores.put(paramString, paramIntRange);
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemCondition build() {
/* 83 */     return new EntityHasScoreCondition((Map<String, IntRange>)this.scores.build(), this.entityTarget);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\EntityHasScoreCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
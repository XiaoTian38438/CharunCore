/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
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
/* 47 */   private Optional<Long> period = Optional.empty();
/*    */   private final IntRange value;
/*    */   
/*    */   public Builder(IntRange paramIntRange) {
/* 51 */     this.value = paramIntRange;
/*    */   }
/*    */   
/*    */   public Builder setPeriod(long paramLong) {
/* 55 */     this.period = Optional.of(Long.valueOf(paramLong));
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TimeCheck build() {
/* 61 */     return new TimeCheck(this.period, this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\TimeCheck$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
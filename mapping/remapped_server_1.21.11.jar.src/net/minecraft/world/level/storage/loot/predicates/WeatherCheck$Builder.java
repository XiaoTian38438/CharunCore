/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/* 41 */   private Optional<Boolean> isRaining = Optional.empty();
/* 42 */   private Optional<Boolean> isThundering = Optional.empty();
/*    */   
/*    */   public Builder setRaining(boolean paramBoolean) {
/* 45 */     this.isRaining = Optional.of(Boolean.valueOf(paramBoolean));
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public Builder setThundering(boolean paramBoolean) {
/* 50 */     this.isThundering = Optional.of(Boolean.valueOf(paramBoolean));
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatherCheck build() {
/* 56 */     return new WeatherCheck(this.isRaining, this.isThundering);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\predicates\WeatherCheck$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
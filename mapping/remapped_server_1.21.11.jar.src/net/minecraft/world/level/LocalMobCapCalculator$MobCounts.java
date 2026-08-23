/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MobCounts
/*    */ {
/* 48 */   private final Object2IntMap<MobCategory> counts = (Object2IntMap<MobCategory>)new Object2IntOpenHashMap((MobCategory.values()).length);
/*    */   
/*    */   public void add(MobCategory paramMobCategory) {
/* 51 */     this.counts.computeInt(paramMobCategory, (paramMobCategory, paramInteger) -> Integer.valueOf((paramInteger == null) ? 1 : (paramInteger.intValue() + 1)));
/*    */   }
/*    */   
/*    */   public boolean canSpawn(MobCategory paramMobCategory) {
/* 55 */     return (this.counts.getOrDefault(paramMobCategory, 0) < paramMobCategory.getMaxInstancesPerChunk());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LocalMobCapCalculator$MobCounts.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
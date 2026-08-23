/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class StatsCounter {
/*  9 */   protected final Object2IntMap<Stat<?>> stats = Object2IntMaps.synchronize((Object2IntMap)new Object2IntOpenHashMap());
/*    */   
/*    */   public StatsCounter() {
/* 12 */     this.stats.defaultReturnValue(0);
/*    */   }
/*    */   
/*    */   public void increment(Player paramPlayer, Stat<?> paramStat, int paramInt) {
/* 16 */     int i = (int)Math.min(getValue(paramStat) + paramInt, 2147483647L);
/* 17 */     setValue(paramPlayer, paramStat, i);
/*    */   }
/*    */   
/*    */   public void setValue(Player paramPlayer, Stat<?> paramStat, int paramInt) {
/* 21 */     this.stats.put(paramStat, paramInt);
/*    */   }
/*    */   
/*    */   public <T> int getValue(StatType<T> paramStatType, T paramT) {
/* 25 */     return paramStatType.contains(paramT) ? getValue(paramStatType.get(paramT)) : 0;
/*    */   }
/*    */   
/*    */   public int getValue(Stat<?> paramStat) {
/* 29 */     return this.stats.getInt(paramStat);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\StatsCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
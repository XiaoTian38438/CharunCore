/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class Sensing {
/*    */   private final Mob mob;
/* 12 */   private final IntSet seen = (IntSet)new IntOpenHashSet();
/* 13 */   private final IntSet unseen = (IntSet)new IntOpenHashSet();
/*    */   
/*    */   public Sensing(Mob paramMob) {
/* 16 */     this.mob = paramMob;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 20 */     this.seen.clear();
/* 21 */     this.unseen.clear();
/*    */   }
/*    */   
/*    */   public boolean hasLineOfSight(Entity paramEntity) {
/* 25 */     int i = paramEntity.getId();
/* 26 */     if (this.seen.contains(i)) {
/* 27 */       return true;
/*    */     }
/* 29 */     if (this.unseen.contains(i)) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     ProfilerFiller profilerFiller = Profiler.get();
/* 34 */     profilerFiller.push("hasLineOfSight");
/* 35 */     boolean bool = this.mob.hasLineOfSight(paramEntity);
/* 36 */     profilerFiller.pop();
/* 37 */     if (bool) {
/* 38 */       this.seen.add(i);
/*    */     } else {
/* 40 */       this.unseen.add(i);
/*    */     } 
/* 42 */     return bool;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\Sensing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DummySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {}
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 17 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\DummySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
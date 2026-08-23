/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityTracker implements PositionTracker {
/*    */   private final Entity entity;
/*    */   private final boolean trackEyeHeight;
/*    */   private final boolean targetEyeHeight;
/*    */   
/*    */   public EntityTracker(Entity paramEntity, boolean paramBoolean) {
/* 18 */     this(paramEntity, paramBoolean, false);
/*    */   }
/*    */   
/*    */   public EntityTracker(Entity paramEntity, boolean paramBoolean1, boolean paramBoolean2) {
/* 22 */     this.entity = paramEntity;
/* 23 */     this.trackEyeHeight = paramBoolean1;
/* 24 */     this.targetEyeHeight = paramBoolean2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 currentPosition() {
/* 29 */     return this.trackEyeHeight ? this.entity.position().add(0.0D, this.entity.getEyeHeight(), 0.0D) : this.entity.position();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 34 */     return this.targetEyeHeight ? BlockPos.containing((Position)this.entity.getEyePosition()) : this.entity.blockPosition();
/*    */   }
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity paramLivingEntity) {
/*    */     LivingEntity livingEntity;
/* 39 */     Entity entity = this.entity; if (entity instanceof LivingEntity) { livingEntity = (LivingEntity)entity; }
/* 40 */     else { return true; }
/*    */ 
/*    */     
/* 43 */     if (!livingEntity.isAlive()) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     Optional<NearestVisibleLivingEntities> optional = paramLivingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/* 48 */     return (optional.isPresent() && ((NearestVisibleLivingEntities)optional.get()).contains(livingEntity));
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 52 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "EntityTracker for " + String.valueOf(this.entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\EntityTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
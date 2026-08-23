/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityHitResult extends HitResult {
/*    */   private final Entity entity;
/*    */   
/*    */   public EntityHitResult(Entity paramEntity) {
/*  9 */     this(paramEntity, paramEntity.position());
/*    */   }
/*    */   
/*    */   public EntityHitResult(Entity paramEntity, Vec3 paramVec3) {
/* 13 */     super(paramVec3);
/*    */     
/* 15 */     this.entity = paramEntity;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 19 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public HitResult.Type getType() {
/* 24 */     return HitResult.Type.ENTITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\EntityHitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
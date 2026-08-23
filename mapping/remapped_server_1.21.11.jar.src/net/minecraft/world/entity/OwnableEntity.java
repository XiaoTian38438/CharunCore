/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface OwnableEntity
/*    */ {
/*    */   EntityReference<LivingEntity> getOwnerReference();
/*    */   
/*    */   Level level();
/*    */   
/*    */   default LivingEntity getOwner() {
/* 15 */     return EntityReference.getLivingEntity(getOwnerReference(), level());
/*    */   }
/*    */ 
/*    */   
/*    */   default LivingEntity getRootOwner() {
/* 20 */     ObjectArraySet<OwnableEntity> objectArraySet = new ObjectArraySet();
/* 21 */     LivingEntity livingEntity = getOwner();
/* 22 */     objectArraySet.add(this);
/* 23 */     while (livingEntity instanceof OwnableEntity) { OwnableEntity ownableEntity = (OwnableEntity)livingEntity;
/* 24 */       LivingEntity livingEntity1 = ownableEntity.getOwner();
/* 25 */       if (objectArraySet.contains(livingEntity1)) {
/* 26 */         return null;
/*    */       }
/* 28 */       objectArraySet.add(livingEntity);
/* 29 */       livingEntity = ownableEntity.getOwner(); }
/*    */     
/* 31 */     return livingEntity;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\OwnableEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
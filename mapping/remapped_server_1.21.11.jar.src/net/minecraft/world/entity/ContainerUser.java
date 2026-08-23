/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*    */ 
/*    */ public interface ContainerUser
/*    */ {
/*    */   boolean hasContainerOpen(ContainerOpenersCounter paramContainerOpenersCounter, BlockPos paramBlockPos);
/*    */   
/*    */   double getContainerInteractionRange();
/*    */   
/*    */   default LivingEntity getLivingEntity() {
/* 13 */     if (this instanceof LivingEntity) {
/* 14 */       return (LivingEntity)this;
/*    */     }
/* 16 */     throw new IllegalStateException("A container user must be a LivingEntity");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ContainerUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
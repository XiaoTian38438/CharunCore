/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Fallable
/*    */ {
/*    */   default void onLand(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, FallingBlockEntity paramFallingBlockEntity) {}
/*    */   
/*    */   default void onBrokenAfterFall(Level paramLevel, BlockPos paramBlockPos, FallingBlockEntity paramFallingBlockEntity) {}
/*    */   
/*    */   default DamageSource getFallDamageSource(Entity paramEntity) {
/* 19 */     return paramEntity.damageSources().fallingBlock(paramEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\Fallable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
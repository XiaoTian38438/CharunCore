/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DropperBlockEntity extends DispenserBlockEntity {
/*  8 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.dropper");
/*    */   
/*    */   public DropperBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 11 */     super(BlockEntityType.DROPPER, paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component getDefaultName() {
/* 16 */     return DEFAULT_NAME;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\DropperBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
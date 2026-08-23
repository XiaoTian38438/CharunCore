/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedBlockEntity
/*    */   extends BlockEntity {
/*    */   public BedBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 13 */     this(paramBlockPos, paramBlockState, ((BedBlock)paramBlockState.getBlock()).getColor());
/*    */   }
/*    */   private final DyeColor color;
/*    */   public BedBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState, DyeColor paramDyeColor) {
/* 17 */     super(BlockEntityType.BED, paramBlockPos, paramBlockState);
/* 18 */     this.color = paramDyeColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 23 */     return ClientboundBlockEntityDataPacket.create(this);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 27 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BedBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
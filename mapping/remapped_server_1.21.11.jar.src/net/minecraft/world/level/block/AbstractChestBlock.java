/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class AbstractChestBlock<E extends BlockEntity> extends BaseEntityBlock {
/*    */   protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
/*    */   
/*    */   protected AbstractChestBlock(BlockBehaviour.Properties paramProperties, Supplier<BlockEntityType<? extends E>> paramSupplier) {
/* 17 */     super(paramProperties);
/* 18 */     this.blockEntityType = paramSupplier;
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractChestBlock<E>> codec();
/*    */   
/*    */   public abstract DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AbstractChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
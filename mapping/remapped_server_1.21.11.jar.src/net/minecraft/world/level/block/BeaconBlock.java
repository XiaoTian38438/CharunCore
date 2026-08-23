/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
/* 19 */   public static final MapCodec<BeaconBlock> CODEC = simpleCodec(BeaconBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BeaconBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/*    */   public BeaconBlock(BlockBehaviour.Properties paramProperties) {
/* 27 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 32 */     return DyeColor.WHITE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 37 */     return (BlockEntity)new BeaconBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 42 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.BEACON, BeaconBlockEntity::tick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 47 */     if (!paramLevel.isClientSide()) { BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof BeaconBlockEntity) { BeaconBlockEntity beaconBlockEntity = (BeaconBlockEntity)blockEntity;
/* 48 */         paramPlayer.openMenu((MenuProvider)beaconBlockEntity);
/* 49 */         paramPlayer.awardStat(Stats.INTERACT_WITH_BEACON); }
/*    */        }
/* 51 */      return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BeaconBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
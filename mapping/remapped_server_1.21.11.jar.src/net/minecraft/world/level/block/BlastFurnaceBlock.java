/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlastFurnaceBlock extends AbstractFurnaceBlock {
/* 22 */   public static final MapCodec<BlastFurnaceBlock> CODEC = simpleCodec(BlastFurnaceBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BlastFurnaceBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected BlastFurnaceBlock(BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 35 */     return (BlockEntity)new BlastFurnaceBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 40 */     return createFurnaceTicker(paramLevel, paramBlockEntityType, BlockEntityType.BLAST_FURNACE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void openContainer(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 45 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 46 */     if (blockEntity instanceof BlastFurnaceBlockEntity) {
/* 47 */       paramPlayer.openMenu((MenuProvider)blockEntity);
/* 48 */       paramPlayer.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 54 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 58 */     double d1 = paramBlockPos.getX() + 0.5D;
/* 59 */     double d2 = paramBlockPos.getY();
/* 60 */     double d3 = paramBlockPos.getZ() + 0.5D;
/*    */     
/* 62 */     if (paramRandomSource.nextDouble() < 0.1D) {
/* 63 */       paramLevel.playLocalSound(d1, d2, d3, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*    */     }
/*    */     
/* 66 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 67 */     Direction.Axis axis = direction.getAxis();
/*    */     
/* 69 */     double d4 = 0.52D;
/* 70 */     double d5 = paramRandomSource.nextDouble() * 0.6D - 0.3D;
/*    */     
/* 72 */     double d6 = (axis == Direction.Axis.X) ? (direction.getStepX() * 0.52D) : d5;
/* 73 */     double d7 = paramRandomSource.nextDouble() * 9.0D / 16.0D;
/* 74 */     double d8 = (axis == Direction.Axis.Z) ? (direction.getStepZ() * 0.52D) : d5;
/*    */     
/* 76 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1 + d6, d2 + d7, d3 + d8, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BlastFurnaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
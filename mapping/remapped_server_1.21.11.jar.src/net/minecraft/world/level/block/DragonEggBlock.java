/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DragonEggBlock extends FallingBlock {
/* 20 */   public static final MapCodec<DragonEggBlock> CODEC = simpleCodec(DragonEggBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<DragonEggBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/*    */   public DragonEggBlock(BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 35 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 40 */     teleport(paramBlockState, paramLevel, paramBlockPos);
/* 41 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void attack(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 46 */     teleport(paramBlockState, paramLevel, paramBlockPos);
/*    */   }
/*    */   
/*    */   private void teleport(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 50 */     WorldBorder worldBorder = paramLevel.getWorldBorder();
/* 51 */     for (byte b = 0; b < 'Ϩ'; b++) {
/* 52 */       BlockPos blockPos = paramBlockPos.offset(paramLevel.random
/* 53 */           .nextInt(16) - paramLevel.random.nextInt(16), paramLevel.random
/* 54 */           .nextInt(8) - paramLevel.random.nextInt(8), paramLevel.random
/* 55 */           .nextInt(16) - paramLevel.random.nextInt(16));
/*    */       
/* 57 */       if (paramLevel.getBlockState(blockPos).isAir() && worldBorder.isWithinBounds(blockPos) && !paramLevel.isOutsideBuildHeight(blockPos)) {
/* 58 */         if (paramLevel.isClientSide()) {
/* 59 */           for (byte b1 = 0; b1 < ''; b1++) {
/* 60 */             double d1 = paramLevel.random.nextDouble();
/* 61 */             float f1 = (paramLevel.random.nextFloat() - 0.5F) * 0.2F;
/* 62 */             float f2 = (paramLevel.random.nextFloat() - 0.5F) * 0.2F;
/* 63 */             float f3 = (paramLevel.random.nextFloat() - 0.5F) * 0.2F;
/*    */             
/* 65 */             double d2 = Mth.lerp(d1, blockPos.getX(), paramBlockPos.getX()) + paramLevel.random.nextDouble() - 0.5D + 0.5D;
/* 66 */             double d3 = Mth.lerp(d1, blockPos.getY(), paramBlockPos.getY()) + paramLevel.random.nextDouble() - 0.5D;
/* 67 */             double d4 = Mth.lerp(d1, blockPos.getZ(), paramBlockPos.getZ()) + paramLevel.random.nextDouble() - 0.5D + 0.5D;
/* 68 */             paramLevel.addParticle((ParticleOptions)ParticleTypes.PORTAL, d2, d3, d4, f1, f2, f3);
/*    */           } 
/*    */         } else {
/* 71 */           paramLevel.setBlock(blockPos, paramBlockState, 2);
/* 72 */           paramLevel.removeBlock(paramBlockPos, false);
/*    */         } 
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getDelayAfterPlace() {
/* 81 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 86 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 91 */     return -16777216;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DragonEggBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
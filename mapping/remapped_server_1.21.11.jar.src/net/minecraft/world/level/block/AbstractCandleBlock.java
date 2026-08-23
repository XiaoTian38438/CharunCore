/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class AbstractCandleBlock extends Block {
/* 29 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   public static final int LIGHT_PER_CANDLE = 3;
/*    */ 
/*    */   
/*    */   protected AbstractCandleBlock(BlockBehaviour.Properties paramProperties) {
/* 35 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isLit(BlockState paramBlockState) {
/* 41 */     return (paramBlockState.hasProperty((Property)LIT) && (paramBlockState.is(BlockTags.CANDLES) || paramBlockState.is(BlockTags.CANDLE_CAKES)) && ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 46 */     if (!paramLevel.isClientSide() && paramProjectile.isOnFire() && canBeLit(paramBlockState)) {
/* 47 */       setLit((LevelAccessor)paramLevel, paramBlockState, paramBlockHitResult.getBlockPos(), true);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean canBeLit(BlockState paramBlockState) {
/* 52 */     return !((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 57 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 61 */     getParticleOffsets(paramBlockState).forEach(paramVec3 -> addParticlesAndSound(paramLevel, paramVec3.add(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ()), paramRandomSource));
/*    */   }
/*    */   
/*    */   private static void addParticlesAndSound(Level paramLevel, Vec3 paramVec3, RandomSource paramRandomSource) {
/* 65 */     float f = paramRandomSource.nextFloat();
/* 66 */     if (f < 0.3F) {
/* 67 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, paramVec3.x, paramVec3.y, paramVec3.z, 0.0D, 0.0D, 0.0D);
/* 68 */       if (f < 0.17F) {
/* 69 */         paramLevel.playLocalSound(paramVec3.x + 0.5D, paramVec3.y + 0.5D, paramVec3.z + 0.5D, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + paramRandomSource.nextFloat(), paramRandomSource.nextFloat() * 0.7F + 0.3F, false);
/*    */       }
/*    */     } 
/* 72 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMALL_FLAME, paramVec3.x, paramVec3.y, paramVec3.z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */   
/*    */   public static void extinguish(Player paramPlayer, BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 76 */     setLit(paramLevelAccessor, paramBlockState, paramBlockPos, false);
/* 77 */     if (paramBlockState.getBlock() instanceof AbstractCandleBlock) {
/* 78 */       ((AbstractCandleBlock)paramBlockState.getBlock()).getParticleOffsets(paramBlockState).forEach(paramVec3 -> paramLevelAccessor.addParticle((ParticleOptions)ParticleTypes.SMOKE, paramBlockPos.getX() + paramVec3.x(), paramBlockPos.getY() + paramVec3.y(), paramBlockPos.getZ() + paramVec3.z(), 0.0D, 0.10000000149011612D, 0.0D));
/*    */     }
/* 80 */     paramLevelAccessor.playSound(null, paramBlockPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 81 */     paramLevelAccessor.gameEvent((Entity)paramPlayer, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*    */   }
/*    */   
/*    */   private static void setLit(LevelAccessor paramLevelAccessor, BlockState paramBlockState, BlockPos paramBlockPos, boolean paramBoolean) {
/* 85 */     paramLevelAccessor.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)LIT, Boolean.valueOf(paramBoolean)), 11);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onExplosionHit(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, Explosion paramExplosion, BiConsumer<ItemStack, BlockPos> paramBiConsumer) {
/* 90 */     if (paramExplosion.canTriggerBlocks() && ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/* 91 */       extinguish(null, paramBlockState, (LevelAccessor)paramServerLevel, paramBlockPos);
/*    */     }
/* 93 */     super.onExplosionHit(paramBlockState, paramServerLevel, paramBlockPos, paramExplosion, paramBiConsumer);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractCandleBlock> codec();
/*    */   
/*    */   protected abstract Iterable<Vec3> getParticleOffsets(BlockState paramBlockState);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AbstractCandleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
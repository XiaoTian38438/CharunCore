/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class AmethystBlock extends Block {
/* 13 */   public static final MapCodec<AmethystBlock> CODEC = simpleCodec(AmethystBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends AmethystBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public AmethystBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 26 */     if (!paramLevel.isClientSide()) {
/* 27 */       BlockPos blockPos = paramBlockHitResult.getBlockPos();
/* 28 */       paramLevel.playSound(null, blockPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.5F + paramLevel.random.nextFloat() * 1.2F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\AmethystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
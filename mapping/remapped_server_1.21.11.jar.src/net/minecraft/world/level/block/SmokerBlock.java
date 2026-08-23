/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SmokerBlock extends AbstractFurnaceBlock {
/* 21 */   public static final MapCodec<SmokerBlock> CODEC = simpleCodec(SmokerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SmokerBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SmokerBlock(BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 34 */     return (BlockEntity)new SmokerBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 39 */     return createFurnaceTicker(paramLevel, paramBlockEntityType, BlockEntityType.SMOKER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void openContainer(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer) {
/* 44 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 45 */     if (blockEntity instanceof SmokerBlockEntity) {
/* 46 */       paramPlayer.openMenu((MenuProvider)blockEntity);
/* 47 */       paramPlayer.awardStat(Stats.INTERACT_WITH_SMOKER);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 53 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*    */       return;
/*    */     }
/*    */     
/* 57 */     double d1 = paramBlockPos.getX() + 0.5D;
/* 58 */     double d2 = paramBlockPos.getY();
/* 59 */     double d3 = paramBlockPos.getZ() + 0.5D;
/*    */     
/* 61 */     if (paramRandomSource.nextDouble() < 0.1D) {
/* 62 */       paramLevel.playLocalSound(d1, d2, d3, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*    */     }
/*    */     
/* 65 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2 + 1.1D, d3, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SmokerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
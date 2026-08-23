/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MangroveLeavesBlock extends TintedParticleLeavesBlock implements BonemealableBlock {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, MangroveLeavesBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<MangroveLeavesBlock> CODEC;
/*    */   
/*    */   public MapCodec<MangroveLeavesBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MangroveLeavesBlock(float paramFloat, BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramFloat, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 30 */     return paramLevelReader.getBlockState(paramBlockPos.below()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 40 */     paramServerLevel.setBlock(paramBlockPos.below(), MangrovePropaguleBlock.createNewHangingPropagule(), 2);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getParticlePos(BlockPos paramBlockPos) {
/* 45 */     return paramBlockPos.below();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MangroveLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
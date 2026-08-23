/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ColorParticleOption;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class TintedParticleLeavesBlock extends LeavesBlock {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, TintedParticleLeavesBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<TintedParticleLeavesBlock> CODEC;
/*    */   
/*    */   public TintedParticleLeavesBlock(float paramFloat, BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramFloat, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 25 */     ColorParticleOption colorParticleOption = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, paramLevel.getClientLeafTintColor(paramBlockPos));
/* 26 */     ParticleUtils.spawnParticleBelow(paramLevel, paramBlockPos, paramRandomSource, (ParticleOptions)colorParticleOption);
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<? extends TintedParticleLeavesBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TintedParticleLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
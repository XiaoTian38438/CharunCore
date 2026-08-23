/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class UntintedParticleLeavesBlock extends LeavesBlock {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)ParticleTypes.CODEC.fieldOf("leaf_particle").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, UntintedParticleLeavesBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<UntintedParticleLeavesBlock> CODEC;
/*    */   
/*    */   protected final ParticleOptions leafParticle;
/*    */   
/*    */   public UntintedParticleLeavesBlock(float paramFloat, ParticleOptions paramParticleOptions, BlockBehaviour.Properties paramProperties) {
/* 23 */     super(paramFloat, paramProperties);
/* 24 */     this.leafParticle = paramParticleOptions;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 29 */     ParticleUtils.spawnParticleBelow(paramLevel, paramBlockPos, paramRandomSource, this.leafParticle);
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<UntintedParticleLeavesBlock> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\UntintedParticleLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ 
/*    */ public class SpikeConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)Codec.BOOL.fieldOf("crystal_invulnerable").orElse(Boolean.valueOf(false)).forGetter(()), (App)SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(()), (App)BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(())).apply((Applicative)paramInstance, SpikeConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SpikeConfiguration> CODEC;
/*    */   
/*    */   private final boolean crystalInvulnerable;
/*    */   private final List<SpikeFeature.EndSpike> spikes;
/*    */   private final BlockPos crystalBeamTarget;
/*    */   
/*    */   public SpikeConfiguration(boolean paramBoolean, List<SpikeFeature.EndSpike> paramList, BlockPos paramBlockPos) {
/* 24 */     this(paramBoolean, paramList, Optional.ofNullable(paramBlockPos));
/*    */   }
/*    */   
/*    */   private SpikeConfiguration(boolean paramBoolean, List<SpikeFeature.EndSpike> paramList, Optional<BlockPos> paramOptional) {
/* 28 */     this.crystalInvulnerable = paramBoolean;
/* 29 */     this.spikes = paramList;
/* 30 */     this.crystalBeamTarget = paramOptional.orElse(null);
/*    */   }
/*    */   
/*    */   public boolean isCrystalInvulnerable() {
/* 34 */     return this.crystalInvulnerable;
/*    */   }
/*    */   
/*    */   public List<SpikeFeature.EndSpike> getSpikes() {
/* 38 */     return this.spikes;
/*    */   }
/*    */   
/*    */   public BlockPos getCrystalBeamTarget() {
/* 42 */     return this.crystalBeamTarget;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpikeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
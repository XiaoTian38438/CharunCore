/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class SpringConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)FluidState.CODEC.fieldOf("state").forGetter(()), (App)Codec.BOOL.fieldOf("requires_block_below").orElse(Boolean.valueOf(true)).forGetter(()), (App)Codec.INT.fieldOf("rock_count").orElse(Integer.valueOf(4)).forGetter(()), (App)Codec.INT.fieldOf("hole_count").orElse(Integer.valueOf(1)).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("valid_blocks").forGetter(())).apply((Applicative)paramInstance, SpringConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SpringConfiguration> CODEC;
/*    */   
/*    */   public final FluidState state;
/*    */   
/*    */   public final boolean requiresBlockBelow;
/*    */   
/*    */   public final int rockCount;
/*    */   public final int holeCount;
/*    */   public final HolderSet<Block> validBlocks;
/*    */   
/*    */   public SpringConfiguration(FluidState paramFluidState, boolean paramBoolean, int paramInt1, int paramInt2, HolderSet<Block> paramHolderSet) {
/* 27 */     this.state = paramFluidState;
/* 28 */     this.requiresBlockBelow = paramBoolean;
/* 29 */     this.rockCount = paramInt1;
/* 30 */     this.holeCount = paramInt2;
/* 31 */     this.validBlocks = paramHolderSet;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpringConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
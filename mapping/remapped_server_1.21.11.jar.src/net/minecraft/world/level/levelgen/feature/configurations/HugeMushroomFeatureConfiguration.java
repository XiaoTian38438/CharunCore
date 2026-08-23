/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class HugeMushroomFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(()), (App)Codec.INT.fieldOf("foliage_radius").orElse(Integer.valueOf(2)).forGetter(())).apply((Applicative)paramInstance, HugeMushroomFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<HugeMushroomFeatureConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider capProvider;
/*    */   public final BlockStateProvider stemProvider;
/*    */   public final int foliageRadius;
/*    */   
/*    */   public HugeMushroomFeatureConfiguration(BlockStateProvider paramBlockStateProvider1, BlockStateProvider paramBlockStateProvider2, int paramInt) {
/* 19 */     this.capProvider = paramBlockStateProvider1;
/* 20 */     this.stemProvider = paramBlockStateProvider2;
/* 21 */     this.foliageRadius = paramInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\HugeMushroomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
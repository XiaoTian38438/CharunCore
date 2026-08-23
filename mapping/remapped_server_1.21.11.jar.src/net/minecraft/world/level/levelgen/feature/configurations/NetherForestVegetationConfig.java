/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class NetherForestVegetationConfig extends BlockPileConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter(())).apply((Applicative)paramInstance, NetherForestVegetationConfig::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<NetherForestVegetationConfig> CODEC;
/*    */   
/*    */   public final int spreadWidth;
/*    */   public final int spreadHeight;
/*    */   
/*    */   public NetherForestVegetationConfig(BlockStateProvider paramBlockStateProvider, int paramInt1, int paramInt2) {
/* 19 */     super(paramBlockStateProvider);
/* 20 */     this.spreadWidth = paramInt1;
/* 21 */     this.spreadHeight = paramInt2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\NetherForestVegetationConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
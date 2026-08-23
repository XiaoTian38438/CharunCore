/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class EndGatewayConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockPos.CODEC.optionalFieldOf("exit").forGetter(()), (App)Codec.BOOL.fieldOf("exact").forGetter(())).apply((Applicative)paramInstance, EndGatewayConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<EndGatewayConfiguration> CODEC;
/*    */   private final Optional<BlockPos> exit;
/*    */   private final boolean exact;
/*    */   
/*    */   private EndGatewayConfiguration(Optional<BlockPos> paramOptional, boolean paramBoolean) {
/* 19 */     this.exit = paramOptional;
/* 20 */     this.exact = paramBoolean;
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration knownExit(BlockPos paramBlockPos, boolean paramBoolean) {
/* 24 */     return new EndGatewayConfiguration(Optional.of(paramBlockPos), paramBoolean);
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration delayedExitSearch() {
/* 28 */     return new EndGatewayConfiguration(Optional.empty(), false);
/*    */   }
/*    */   
/*    */   public Optional<BlockPos> getExit() {
/* 32 */     return this.exit;
/*    */   }
/*    */   
/*    */   public boolean isExitExact() {
/* 36 */     return this.exact;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\EndGatewayConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
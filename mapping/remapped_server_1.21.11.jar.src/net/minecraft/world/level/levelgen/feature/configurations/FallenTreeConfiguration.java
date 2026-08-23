/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*    */ 
/*    */ public class FallenTreeConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)IntProvider.codec(0, 16).fieldOf("log_length").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(())).apply((Applicative)paramInstance, FallenTreeConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FallenTreeConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider trunkProvider;
/*    */   
/*    */   public final IntProvider logLength;
/*    */   public final List<TreeDecorator> stumpDecorators;
/*    */   public final List<TreeDecorator> logDecorators;
/*    */   
/*    */   protected FallenTreeConfiguration(BlockStateProvider paramBlockStateProvider, IntProvider paramIntProvider, List<TreeDecorator> paramList1, List<TreeDecorator> paramList2) {
/* 26 */     this.trunkProvider = paramBlockStateProvider;
/* 27 */     this.logLength = paramIntProvider;
/* 28 */     this.stumpDecorators = paramList1;
/* 29 */     this.logDecorators = paramList2;
/*    */   }
/*    */   
/*    */   public static class FallenTreeConfigurationBuilder {
/*    */     private final BlockStateProvider trunkProvider;
/*    */     private final IntProvider logLength;
/* 35 */     private List<TreeDecorator> stumpDecorators = new ArrayList<>();
/* 36 */     private List<TreeDecorator> logDecorators = new ArrayList<>();
/*    */     
/*    */     public FallenTreeConfigurationBuilder(BlockStateProvider param1BlockStateProvider, IntProvider param1IntProvider) {
/* 39 */       this.trunkProvider = param1BlockStateProvider;
/* 40 */       this.logLength = param1IntProvider;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> param1List) {
/* 44 */       this.stumpDecorators = param1List;
/* 45 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> param1List) {
/* 49 */       this.logDecorators = param1List;
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfiguration build() {
/* 54 */       return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\FallenTreeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
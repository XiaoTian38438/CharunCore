/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FallenTreeConfigurationBuilder
/*    */ {
/*    */   private final BlockStateProvider trunkProvider;
/*    */   private final IntProvider logLength;
/* 35 */   private List<TreeDecorator> stumpDecorators = new ArrayList<>();
/* 36 */   private List<TreeDecorator> logDecorators = new ArrayList<>();
/*    */   
/*    */   public FallenTreeConfigurationBuilder(BlockStateProvider paramBlockStateProvider, IntProvider paramIntProvider) {
/* 39 */     this.trunkProvider = paramBlockStateProvider;
/* 40 */     this.logLength = paramIntProvider;
/*    */   }
/*    */   
/*    */   public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> paramList) {
/* 44 */     this.stumpDecorators = paramList;
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> paramList) {
/* 49 */     this.logDecorators = paramList;
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public FallenTreeConfiguration build() {
/* 54 */     return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\configurations\FallenTreeConfiguration$FallenTreeConfigurationBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
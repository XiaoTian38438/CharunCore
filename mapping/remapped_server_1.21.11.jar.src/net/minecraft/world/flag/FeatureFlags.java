/*    */ package net.minecraft.world.flag;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FeatureFlags
/*    */ {
/*    */   public static final FeatureFlag VANILLA;
/*    */   public static final FeatureFlag TRADE_REBALANCE;
/*    */   public static final FeatureFlag REDSTONE_EXPERIMENTS;
/*    */   public static final FeatureFlag MINECART_IMPROVEMENTS;
/*    */   public static final FeatureFlagRegistry REGISTRY;
/*    */   
/*    */   static {
/* 19 */     FeatureFlagRegistry.Builder builder = new FeatureFlagRegistry.Builder("main");
/* 20 */     VANILLA = builder.createVanilla("vanilla");
/* 21 */     TRADE_REBALANCE = builder.createVanilla("trade_rebalance");
/* 22 */     REDSTONE_EXPERIMENTS = builder.createVanilla("redstone_experiments");
/* 23 */     MINECART_IMPROVEMENTS = builder.createVanilla("minecart_improvements");
/*    */ 
/*    */     
/* 26 */     REGISTRY = builder.build();
/*    */   }
/*    */   
/* 29 */   public static final Codec<FeatureFlagSet> CODEC = REGISTRY.codec();
/*    */   
/* 31 */   public static final FeatureFlagSet VANILLA_SET = FeatureFlagSet.of(VANILLA);
/* 32 */   public static final FeatureFlagSet DEFAULT_FLAGS = VANILLA_SET;
/*    */   
/*    */   public static String printMissingFlags(FeatureFlagSet paramFeatureFlagSet1, FeatureFlagSet paramFeatureFlagSet2) {
/* 35 */     return printMissingFlags(REGISTRY, paramFeatureFlagSet1, paramFeatureFlagSet2);
/*    */   }
/*    */   
/*    */   public static String printMissingFlags(FeatureFlagRegistry paramFeatureFlagRegistry, FeatureFlagSet paramFeatureFlagSet1, FeatureFlagSet paramFeatureFlagSet2) {
/* 39 */     Set<Identifier> set1 = paramFeatureFlagRegistry.toNames(paramFeatureFlagSet2);
/* 40 */     Set<Identifier> set2 = paramFeatureFlagRegistry.toNames(paramFeatureFlagSet1);
/* 41 */     return set1.stream().filter(paramIdentifier -> !paramSet.contains(paramIdentifier)).map(Identifier::toString).collect(Collectors.joining(", "));
/*    */   }
/*    */   
/*    */   public static boolean isExperimental(FeatureFlagSet paramFeatureFlagSet) {
/* 45 */     return !paramFeatureFlagSet.isSubsetOf(VANILLA_SET);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\flag\FeatureFlags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
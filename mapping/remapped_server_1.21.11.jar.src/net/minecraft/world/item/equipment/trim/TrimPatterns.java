/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class TrimPatterns {
/* 11 */   public static final ResourceKey<TrimPattern> SENTRY = registryKey("sentry");
/* 12 */   public static final ResourceKey<TrimPattern> DUNE = registryKey("dune");
/* 13 */   public static final ResourceKey<TrimPattern> COAST = registryKey("coast");
/* 14 */   public static final ResourceKey<TrimPattern> WILD = registryKey("wild");
/* 15 */   public static final ResourceKey<TrimPattern> WARD = registryKey("ward");
/* 16 */   public static final ResourceKey<TrimPattern> EYE = registryKey("eye");
/* 17 */   public static final ResourceKey<TrimPattern> VEX = registryKey("vex");
/* 18 */   public static final ResourceKey<TrimPattern> TIDE = registryKey("tide");
/* 19 */   public static final ResourceKey<TrimPattern> SNOUT = registryKey("snout");
/* 20 */   public static final ResourceKey<TrimPattern> RIB = registryKey("rib");
/* 21 */   public static final ResourceKey<TrimPattern> SPIRE = registryKey("spire");
/* 22 */   public static final ResourceKey<TrimPattern> WAYFINDER = registryKey("wayfinder");
/* 23 */   public static final ResourceKey<TrimPattern> SHAPER = registryKey("shaper");
/* 24 */   public static final ResourceKey<TrimPattern> SILENCE = registryKey("silence");
/* 25 */   public static final ResourceKey<TrimPattern> RAISER = registryKey("raiser");
/* 26 */   public static final ResourceKey<TrimPattern> HOST = registryKey("host");
/* 27 */   public static final ResourceKey<TrimPattern> FLOW = registryKey("flow");
/* 28 */   public static final ResourceKey<TrimPattern> BOLT = registryKey("bolt");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<TrimPattern> paramBootstrapContext) {
/* 31 */     register(paramBootstrapContext, SENTRY);
/* 32 */     register(paramBootstrapContext, DUNE);
/* 33 */     register(paramBootstrapContext, COAST);
/* 34 */     register(paramBootstrapContext, WILD);
/* 35 */     register(paramBootstrapContext, WARD);
/* 36 */     register(paramBootstrapContext, EYE);
/* 37 */     register(paramBootstrapContext, VEX);
/* 38 */     register(paramBootstrapContext, TIDE);
/* 39 */     register(paramBootstrapContext, SNOUT);
/* 40 */     register(paramBootstrapContext, RIB);
/* 41 */     register(paramBootstrapContext, SPIRE);
/* 42 */     register(paramBootstrapContext, WAYFINDER);
/* 43 */     register(paramBootstrapContext, SHAPER);
/* 44 */     register(paramBootstrapContext, SILENCE);
/* 45 */     register(paramBootstrapContext, RAISER);
/* 46 */     register(paramBootstrapContext, HOST);
/* 47 */     register(paramBootstrapContext, FLOW);
/* 48 */     register(paramBootstrapContext, BOLT);
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<TrimPattern> paramBootstrapContext, ResourceKey<TrimPattern> paramResourceKey) {
/* 52 */     TrimPattern trimPattern = new TrimPattern(defaultAssetId(paramResourceKey), (Component)Component.translatable(Util.makeDescriptionId("trim_pattern", paramResourceKey.identifier())), false);
/* 53 */     paramBootstrapContext.register(paramResourceKey, trimPattern);
/*    */   }
/*    */   
/*    */   private static ResourceKey<TrimPattern> registryKey(String paramString) {
/* 57 */     return ResourceKey.create(Registries.TRIM_PATTERN, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static Identifier defaultAssetId(ResourceKey<TrimPattern> paramResourceKey) {
/* 61 */     return paramResourceKey.identifier();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\equipment\trim\TrimPatterns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
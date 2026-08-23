/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ public interface GameTestInstances
/*    */ {
/* 12 */   public static final ResourceKey<GameTestInstance> ALWAYS_PASS = create("always_pass");
/*    */   
/*    */   static void bootstrap(BootstrapContext<GameTestInstance> paramBootstrapContext) {
/* 15 */     HolderGetter holderGetter1 = paramBootstrapContext.lookup(Registries.TEST_FUNCTION);
/* 16 */     HolderGetter holderGetter2 = paramBootstrapContext.lookup(Registries.TEST_ENVIRONMENT);
/* 17 */     paramBootstrapContext.register(ALWAYS_PASS, new FunctionGameTestInstance(BuiltinTestFunctions.ALWAYS_PASS, new TestData(holderGetter2.getOrThrow(GameTestEnvironments.DEFAULT_KEY), Identifier.withDefaultNamespace("empty"), 1, 1, false)));
/*    */   }
/*    */   
/*    */   private static ResourceKey<GameTestInstance> create(String paramString) {
/* 21 */     return ResourceKey.create(Registries.TEST_INSTANCE, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestInstances.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
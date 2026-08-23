/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GameTestEnvironments
/*    */ {
/*    */   public static final String DEFAULT = "default";
/* 14 */   public static final ResourceKey<TestEnvironmentDefinition> DEFAULT_KEY = create("default");
/*    */   
/*    */   private static ResourceKey<TestEnvironmentDefinition> create(String paramString) {
/* 17 */     return ResourceKey.create(Registries.TEST_ENVIRONMENT, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   static void bootstrap(BootstrapContext<TestEnvironmentDefinition> paramBootstrapContext) {
/* 21 */     paramBootstrapContext.register(DEFAULT_KEY, new TestEnvironmentDefinition.AllOf(List.of()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestEnvironments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
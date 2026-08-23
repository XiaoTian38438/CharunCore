/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class BuiltinTestFunctions
/*    */   extends TestFunctionLoader {
/* 12 */   public static final ResourceKey<Consumer<GameTestHelper>> ALWAYS_PASS = create("always_pass");
/* 13 */   public static final Consumer<GameTestHelper> ALWAYS_PASS_INSTANCE = GameTestHelper::succeed;
/*    */   
/*    */   private static ResourceKey<Consumer<GameTestHelper>> create(String paramString) {
/* 16 */     return ResourceKey.create(Registries.TEST_FUNCTION, Identifier.withDefaultNamespace(paramString));
/*    */   }
/*    */   
/*    */   public static Consumer<GameTestHelper> bootstrap(Registry<Consumer<GameTestHelper>> paramRegistry) {
/* 20 */     registerLoader(new BuiltinTestFunctions());
/* 21 */     runLoaders(paramRegistry);
/* 22 */     return ALWAYS_PASS_INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> paramBiConsumer) {
/* 27 */     paramBiConsumer.accept(ALWAYS_PASS, ALWAYS_PASS_INSTANCE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\BuiltinTestFunctions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
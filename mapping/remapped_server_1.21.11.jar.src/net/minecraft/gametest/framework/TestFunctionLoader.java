/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public abstract class TestFunctionLoader {
/* 12 */   private static final List<TestFunctionLoader> loaders = new ArrayList<>();
/*    */   
/*    */   public static void registerLoader(TestFunctionLoader paramTestFunctionLoader) {
/* 15 */     loaders.add(paramTestFunctionLoader);
/*    */   }
/*    */   
/*    */   public static void runLoaders(Registry<Consumer<GameTestHelper>> paramRegistry) {
/* 19 */     for (Iterator<TestFunctionLoader> iterator = loaders.iterator(); iterator.hasNext(); ) { TestFunctionLoader testFunctionLoader = iterator.next();
/* 20 */       testFunctionLoader.load((paramResourceKey, paramConsumer) -> Registry.register(paramRegistry, paramResourceKey, paramConsumer)); }
/*    */   
/*    */   }
/*    */   
/*    */   public abstract void load(BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> paramBiConsumer);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\TestFunctionLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
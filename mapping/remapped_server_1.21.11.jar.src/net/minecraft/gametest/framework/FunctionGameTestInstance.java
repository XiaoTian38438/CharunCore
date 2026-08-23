/*    */ package net.minecraft.gametest.framework;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class FunctionGameTestInstance extends GameTestInstance {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ResourceKey.codec(Registries.TEST_FUNCTION).fieldOf("function").forGetter(FunctionGameTestInstance::function), (App)TestData.CODEC.forGetter(GameTestInstance::info)).apply((Applicative)paramInstance, FunctionGameTestInstance::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FunctionGameTestInstance> CODEC;
/*    */   
/*    */   private final ResourceKey<Consumer<GameTestHelper>> function;
/*    */   
/*    */   public FunctionGameTestInstance(ResourceKey<Consumer<GameTestHelper>> paramResourceKey, TestData<Holder<TestEnvironmentDefinition>> paramTestData) {
/* 23 */     super(paramTestData);
/* 24 */     this.function = paramResourceKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(GameTestHelper paramGameTestHelper) {
/* 29 */     ((Consumer<GameTestHelper>)paramGameTestHelper.getLevel().registryAccess().get(this.function)
/* 30 */       .map(Holder.Reference::value)
/* 31 */       .orElseThrow(() -> new IllegalStateException("Trying to access missing test function: " + String.valueOf(this.function.identifier()))))
/* 32 */       .accept(paramGameTestHelper);
/*    */   }
/*    */   
/*    */   private ResourceKey<Consumer<GameTestHelper>> function() {
/* 36 */     return this.function;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<FunctionGameTestInstance> codec() {
/* 41 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MutableComponent typeDescription() {
/* 46 */     return Component.translatable("test_instance.type.function");
/*    */   }
/*    */ 
/*    */   
/*    */   public Component describe() {
/* 51 */     return (Component)describeType()
/* 52 */       .append((Component)descriptionRow("test_instance.description.function", this.function.identifier().toString()))
/* 53 */       .append(describeInfo());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\FunctionGameTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
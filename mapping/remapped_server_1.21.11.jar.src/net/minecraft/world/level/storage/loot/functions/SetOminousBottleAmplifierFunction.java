/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetOminousBottleAmplifierFunction extends LootItemConditionalFunction {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> commonFields(paramInstance).and((App)NumberProviders.CODEC.fieldOf("amplifier").forGetter(())).apply((Applicative)paramInstance, SetOminousBottleAmplifierFunction::new));
/*    */   }
/*    */   
/*    */   static final MapCodec<SetOminousBottleAmplifierFunction> CODEC;
/*    */   private final NumberProvider amplifierGenerator;
/*    */   
/*    */   private SetOminousBottleAmplifierFunction(List<LootItemCondition> paramList, NumberProvider paramNumberProvider) {
/* 26 */     super(paramList);
/* 27 */     this.amplifierGenerator = paramNumberProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 32 */     return this.amplifierGenerator.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetOminousBottleAmplifierFunction> getType() {
/* 37 */     return LootItemFunctions.SET_OMINOUS_BOTTLE_AMPLIFIER;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack paramItemStack, LootContext paramLootContext) {
/* 42 */     int i = Mth.clamp(this.amplifierGenerator.getInt(paramLootContext), 0, 4);
/* 43 */     paramItemStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(i));
/* 44 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   public NumberProvider amplifier() {
/* 48 */     return this.amplifierGenerator;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setAmplifier(NumberProvider paramNumberProvider) {
/* 52 */     return simpleBuilder(paramList -> new SetOminousBottleAmplifierFunction(paramList, paramNumberProvider));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\SetOminousBottleAmplifierFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */